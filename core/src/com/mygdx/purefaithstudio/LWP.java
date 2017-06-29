package com.mygdx.purefaithstudio;

import com.badlogic.gdx.Game;

public class LWP extends Game {
	public Resolver resolver = null;

	@Override
	public void create() {
		setScreen(new com.mygdx.purefaithstudio.Main(this, resolver));
	}

	@Override
	public void dispose () {
		super.dispose();
		resolver = null;
		getScreen().dispose();
	}
}
