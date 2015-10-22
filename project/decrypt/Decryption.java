package com.kryptogram.project.decrypt;

import com.kryptogram.mainactivity.StartActivity;

public class Decryption extends StartActivity {

	public String decrypt(String text, String cypher) {
		String textMinusHex = null;
		if (text.length() > 0) {
			textMinusHex = text.substring(2);
			String hex = text.substring(0, 2);
			final int layers = Integer.parseInt(hex, 16);
			return decryptLayers(textMinusHex, layers, layers, 0, cypher);
		}else{
			return textMinusHex;
		}
	}

	public String decryptLayers(String text, int layers, int layerCounter, int count, String cypher) {
		String decryptedText = "";
		for (int index = 0; index < text.length(); index++) {
			char character = text.charAt(index);
			decryptedText += getPlainChar(character, index, cypher);
		}
		count++;
		layerCounter--;
		if (count < layers) {
			if (layerCounter % FLIP == 0) {
				decryptedText = new StringBuilder(decryptedText).reverse().toString();
				decryptedText = decryptLayers(decryptedText, layers, layerCounter, count, cypher);
			} else {
				decryptedText = decryptLayers(decryptedText, layers, layerCounter, count, cypher);
			}
		}
		return decryptedText;
	}

	private char getPlainChar(char character, int index, String cypher) {
		int newIndex = getNewIndex(character, index, cypher);
		char cypherCharacter = cypher.charAt(newIndex);
		return cypherCharacter;
	}

	private int getNewIndex(char character, int index, String cypherText) {
		int characterIndex = cypherText.indexOf(Character.toString(character));
		int newIndex = ((characterIndex - ((index + ENCRYPT_VAR_1) * ENCRYPT_VAR_2)) % cypherText.length()) + cypherText.length();
		if (newIndex % cypherText.length() == 0) {
			newIndex = 0;
		} else if (newIndex >= cypherText.length()) {
			newIndex %= cypherText.length();
		}
		return newIndex;
	}
}
