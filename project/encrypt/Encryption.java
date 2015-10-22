package com.kryptogram.project.encrypt;

import java.util.Random;

import com.kryptogram.mainactivity.StartActivity;

public class Encryption extends StartActivity{
	public static Random randomNumber = new Random();
	public static int LAYERS = randomNumber.nextInt(256);
	
	public String encrypt(String text, String cypher) {
		int layers = randomNumber.nextInt(256);
		String hex = Integer.toHexString(layers);
		if (hex.length() < 2) {
			hex = "0" + hex;
		}
		return hex + encryptLayers(text, 0, layers, cypher);
	}

	private String encryptLayers(String text, int count, int layers, String cypher) {
		String encryptedText = "";
		for (int index = 0; index < text.length(); index++) {
			char character = text.charAt(index);
			encryptedText += getCypherChar(character, index, cypher);
		}
		count++;
		if (count < layers) {
			if (count % FLIP == 0) {
				encryptedText = new StringBuilder(encryptedText).reverse().toString();
				encryptedText = encryptLayers(encryptedText, count, layers, cypher);
			} else {
				encryptedText = encryptLayers(encryptedText, count, layers, cypher);
			}
		}
		return encryptedText;
	}

	private char getCypherChar(char character, int index, String cypher) {
		int newIndex = getNewIndex(character, index, cypher);
		char cypherCharacter = cypher.charAt(newIndex);
		return cypherCharacter;
	}

	private int getNewIndex(char character, int index, String cypherText) {
		int characterIndex = cypherText.indexOf(Character.toString(character));
		int newIndex = (characterIndex + ((index + ENCRYPT_VAR_1) * ENCRYPT_VAR_2)) % cypherText.length();
		return newIndex;
	}
}
