package com.livraison.supervision.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    // ============================================================
    // CONFIGURATION — modifier selon votre environnement
    // ============================================================

    // IP de votre PC sur le réseau Wi-Fi local
    // Trouvez-la avec : ipconfig (Windows) → "Adresse IPv4"
    // Exemple : 192.168.1.100
    private static final String PC_IP = "169.254.30.42"; // ← CHANGER ICI

    private static final int PORT = 8082; // ← port Spring Boot

    // URL pour émulateur Android Studio (accède au PC via 10.0.2.2)
    private static final String BASE_URL_EMULATEUR = "http://10.0.2.2:" + PORT + "/";

    // URL pour téléphone physique (accède au PC via IP Wi-Fi)
    private static final String BASE_URL_TELEPHONE = "http://" + PC_IP + ":" + PORT + "/";

    // ← true = émulateur | false = téléphone physique
    private static final boolean USE_EMULATEUR = false;

    private static final String BASE_URL =
            USE_EMULATEUR ? BASE_URL_EMULATEUR : BASE_URL_TELEPHONE;

    // ============================================================

    private static RetrofitClient instance;
    private final ApiService api;
    private String token;

    private RetrofitClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();
                    if (token != null) {
                        builder.header("Authorization", "Bearer " + token);
                    }
                    return chain.proceed(builder.build());
                })
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApi() { return api; }

    public void setToken(String token) { this.token = token; }

    public void clearToken() {
        this.token = null;
        instance = null; // reset pour recréer avec nouveau token
    }
}