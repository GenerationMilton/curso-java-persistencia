package com.gatosApp.java_gatos;

import com.gatosApp.java_gatos.model.Cats;
import com.gatosApp.java_gatos.service.CatService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.io.IOException;

@SpringBootApplication
public class JavaGatosApplication {

	public static void main(String[] args) throws IOException {

		int optionMenu= -1;
		String[] botons= {"1. ver gatos", "2. ver favoritos", "3. salir"};

		do{
			//menu principal
			String option = (String)JOptionPane.showInputDialog(null," Gatitos java", "Menu principal",
					JOptionPane.INFORMATION_MESSAGE, null, botons, botons[0]);

			//validar la opcion que selecciona el usuario
			for(int i=0; i<botons.length;i++){
				if(option.equals(botons[i])){
					optionMenu=i;
				}
			}

			switch (optionMenu){
				case 0:
					CatService.viewCats();
					break;
				case 1:
					Cats cat= new Cats();
					CatService.viewFavorite(cat.getApiKey());
				default:
					break;
			}

		}while(optionMenu!=1);

	}

}
