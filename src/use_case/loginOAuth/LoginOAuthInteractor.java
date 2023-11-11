package use_case.loginOAuth;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import interface_adapter.loginOAuth.LoginOAuthController;

import com.sun.net.httpserver.HttpServer;
import entity.SpotifyAuth;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import use_case.login.LoginInteractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Base64;
import java.io.InputStream;


public class LoginOAuthInteractor implements LoginOAuthInputBoundary {
    LoginOAuthUserDataAccessInterface dao;
    LoginOAuthOutputBoundary presenter;

    public LoginOAuthInteractor(LoginOAuthUserDataAccessInterface dao, LoginOAuthOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    public void execute(LoginOAuthInputData data) throws IOException {
        // make the http POST request for the access token


        // code passed in manually, needs to change?
        String code = data.getCode();
        URL url = new URL("https://accounts.spotify.com/api/token?grant_type=authorization_code&code=" +
                code + "&redirect_uri=http://localhost:8080/callback");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        String clientIDAndSecret = SpotifyAuth.getClientId() + ":" + SpotifyAuth.getClientSecret();
        String clientBase64 = new String(Base64.getEncoder().encode(clientIDAndSecret.getBytes()));
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", "Basic " + clientBase64);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setChunkedStreamingMode(286);
        int responseCode = http.getResponseCode();
        System.out.println(http.getResponseMessage());
        // parse the InputStream with json simple package
        if (HttpURLConnection.HTTP_OK == responseCode) {
            InputStream inputStream = http.getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject;
            try {
                jsonObject = (JSONObject) jsonParser.parse(
                        new InputStreamReader(inputStream, "UTF-8"));
                System.out.println(jsonObject.toJSONString());
                SpotifyAuth.setAccessToken((String) jsonObject.get("access_token"));
                SpotifyAuth.setRefreshToken((String) jsonObject.get("refresh_token"));
            } catch (ParseException e) {
                System.out.println("InputStream could not be parsed into JSON object");
            }
            System.out.println(SpotifyAuth.getAccessToken());
            System.out.println(SpotifyAuth.getRefreshToken());
            presenter.prepareSuccessView(new LoginOAuthOutputData());

        } else {
            presenter.prepareFailView(Integer.toString(responseCode), http.getResponseMessage());
        }
        http.disconnect();
    }

    // Methods and classes for creating server to handle redirect
    public static void createServer(int port, LoginOAuthController controller) throws IOException {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/callback", new LoginOAuthInteractor.CallbackHandler(controller));
            server.start();
            System.out.println("Server is listening on port " + port);
        } catch (IOException e) {
            System.out.println("Could not create server");
        }
    }

    static class CallbackHandler implements HttpHandler {
        private final LoginOAuthController controller;

        public CallbackHandler(LoginOAuthController controller) {
            this.controller = controller;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            // Get the authorization code from the query parameters
            URI uri = exchange.getRequestURI();
            String query = uri.getQuery();
            String code = query.split("=")[1]; // Assumes a simple query parameter structure

            // Send a response to the browser
            String response = "Authorization code received. You can close this window.";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            System.out.println("Authorization code: " + code);

            controller.execute(code);

            // Stop the server after handling the callback
            exchange.close();
        }
    }

}