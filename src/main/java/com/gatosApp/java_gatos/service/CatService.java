package com.gatosApp.java_gatos.service;

import com.gatosApp.java_gatos.model.Cats;
import com.gatosApp.java_gatos.model.CatsFavorites;
import com.google.gson.Gson;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatService {
    public static void viewCats() throws IOException {
        //Codigo de postman con el metodo buscar cats
        //1.traer los datos de la API
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        String theJson = response.body().string();

        //2. eliminar el primer caracter y el ultimo del objeto json []
        theJson = theJson.substring(1,theJson.length());
        theJson = theJson.substring(0,theJson.length()-1);

        //3. Crear un objeto de la clase Gson
        Gson gson = new Gson();
        Cats cat= gson.fromJson(theJson, Cats.class);

        //4. redimensionar imagen de la API
        Image image = null;
        URL url = new URL(cat.getUrl());
        image = ImageIO.read(url);

        //5. objeto Imagen Icon
        ImageIcon backgroundCat = new ImageIcon(image);
        if(backgroundCat.getIconWidth()>800){
            //redimensionar
            Image background = backgroundCat.getImage();
            Image modified = background.getScaledInstance(800,600, Image.SCALE_SMOOTH);
            backgroundCat = new ImageIcon(modified);
        }

        //MENU DE OPCIONES
        String menu = "Opciones:  \n"
                + " 1. ver otra imagen \n"
                + " 2. Favorito \n"
                + " 3. Volver \n";
        
        String[] botons={"ver otra imagen", "favorito", "volver"};
        String idCat= cat.getId();
        String option=(String) JOptionPane.showInputDialog(null,menu, idCat, JOptionPane.INFORMATION_MESSAGE, backgroundCat,botons,botons[0]);

        int selection=-1;
        //validamos que opcion selecciona el usuario
        for(int i=0;i<botons.length;i++){
            if(option.equals(botons[i])){
                selection=i;
            }
        }
        
        switch (selection){
            case 0:
                viewCats();
                break;
            case 1:
                favoriteCats(cat);
                break;
            default:
                break;
        }

    }

    public static void favoriteCats(Cats cat) throws IOException {

        //Codigo de postman con el metodo Crear Favoritos
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\":\""+ cat.getId()+ "\"\r\n    \n}");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("POST", body)
                .addHeader("content-type", "application/json")
                .addHeader("x-api-key", cat.getApiKey())
                .build();
        Response response = client.newCall(request).execute();

    }

    public static void viewFavorite(String apiKey) throws IOException {

        //codigo de postman con el metodo GET listar favoritos
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .build();
        Response response = client.newCall(request).execute();

        //Capturar la respuesta de la peticion de la API
        String theJson = response.body().string();

        //Creamos el objeto gson
        Gson gson = new Gson();

        //Logica para agregar gatos al array del json
        CatsFavorites[] catsArray = gson.fromJson(theJson, CatsFavorites[].class);

        if(catsArray.length>0){
            int min =1;
            int max = catsArray.length;
            int random= (int)(Math.random() * ((max-min)+1)) + min;
            int index = random-1;
            CatsFavorites catFavorite = catsArray[index];

            //4. redimensionar imagen de la API
            Image image = null;
            URL url = new URL(catFavorite.getImage().getUrl());
            image = ImageIO.read(url);

            //5. objeto Imagen Icon
            ImageIcon backgroundCat = new ImageIcon(image);
            if(backgroundCat.getIconWidth()>800){
                //redimensionar
                Image background = backgroundCat.getImage();
                Image modified = background.getScaledInstance(800,600, Image.SCALE_SMOOTH);
                backgroundCat = new ImageIcon(modified);
            }

            //MENU DE OPCIONES
            String menu = "Opciones:  \n"
                    + " 1. ver otra imagen \n"
                    + " 2. Eliminar Favorito \n"
                    + " 3. Volver \n";

            String[] botons={"ver otra imagen", " eliminar favorito", "volver"};
            String idCat= catFavorite.getId();
            String option=(String) JOptionPane.showInputDialog(null,menu, idCat, JOptionPane.INFORMATION_MESSAGE, backgroundCat,botons,botons[0]);

            int selection=-1;
            //validamos que opcion selecciona el usuario
            for(int i=0;i<botons.length;i++){
                if(option.equals(botons[i])){
                    selection=i;
                }
            }

            switch (selection){
                case 0:
                    viewFavorite(apiKey);
                    break;
                case 1:
                    deleteFavorite(catFavorite);
                    break;
                default:
                    break;
            }

        }


    }

    public static void deleteFavorite(CatsFavorites catFavorite) throws IOException {
        //codigo postman con metodo delete de la api documentacion
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites/"+catFavorite.getId()+"")
                .method("DELETE", body)
                .addHeader("x-api-key", catFavorite.getApiKey())
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
    }
}
