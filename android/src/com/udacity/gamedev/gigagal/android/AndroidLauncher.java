package com.udacity.gamedev.gigagal.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.udacity.gamedev.gigagal.app.GigaGalGame;
import com.udacity.gamedev.gigagal.entities.GigaGal;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(GigaGalGame.getInstance(), config);
	}
}
