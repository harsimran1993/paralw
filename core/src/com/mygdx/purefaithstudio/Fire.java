/*package com.mygdx.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;

public class Fire {
	ParticleEffect pe;
	String EveNo = "1";
	int LWcase = 0;
	private float velx, vely, zigzag;
	private float lastchng = 0, posx = 240, posy = 400;
	private double ang45 = Math.PI * 0.5f;
	private double angle;// = Math.atan2(vely, velx);
	private double cos;// = Math.cos(angle);
	private double sin;// = Math.sin(angle);
	ScaledNumericValue angles=null;//

	public Fire(float x, float y) {
		pe = new ParticleEffect();
		velx = 150;
		vely = 150;
		zigzag = 100;
		angle = Math.atan2(vely, velx);
		cos = Math.cos(angle + ang45);
		sin = Math.sin(angle + ang45);
		// System.out.println(Config.listTest);
		EveNo = Config.listTest;
		LWcase = Integer.parseInt(Config.listTest);
		switch (LWcase) {
		case 0:
			pe.load(Gdx.files.internal("particle/enemage.party"), Gdx.files.internal("particle"));
			angles = pe.getEmitters().first().getAngle();
			angles.setHigh(240, 300);
			angles.setLow(270);
			break;
		case 1:
			pe.load(Gdx.files.internal("particle/design4.party"), Gdx.files.internal("particle"));
			break;
		case 2:
			pe.load(Gdx.files.internal("particle/design5.party"), Gdx.files.internal("particle"));
			break;
		case 3:
			pe.load(Gdx.files.internal("particle/mage.party"), Gdx.files.internal("particle"));
			angles = pe.getEmitters().first().getAngle();
			angles.setHigh(240, 300);
			angles.setLow(270);
			break;
		case 4:
			pe.load(Gdx.files.internal("particle/chakra.party"), Gdx.files.internal("particle"));
			break;
		default:
			pe.load(Gdx.files.internal("particle/mage.party"), Gdx.files.internal("particle"));
			angles = pe.getEmitters().first().getAngle();
			angles.setHigh(240, 300);
			angles.setLow(270);
		}
		pe.setPosition(x, y);
		if(angles == null)
			angles = pe.getEmitters().first().getAngle();
		pe.start();
		// pe.scaleEffect(-1f);

	}
	public void setColor(float [] colors){
		for(ParticleEmitter em:pe.getEmitters()){
			em.getTint().setColors(colors);
		}
	}
	public void updateold(float delta) {
		if (Config.moving) {
			posx += (velx + zigzag * cos) * delta;
			posy += (vely + zigzag * sin) * delta;
			if (posx > 480 || posx < 0) {
				if (posx > 480)
					posx = 480;
				else
					posx = 0;
				velx *= -1;
				angle = Math.atan2(vely, velx);
				cos = Math.cos(angle + ang45);
				sin = Math.sin(angle + ang45);
				angle = Math.toDegrees(angle);
				angles.setHigh((float) (angle - 30.0f), (float) (angle + 30.0f));
				angles.setLow((float) angle);
			}
			if (posy > 800 || posy < 0) {
				if (posy > 800)
					posy = 800;
				else
					posy = 0;
				vely *= -1;
				angle = Math.toDegrees(Math.atan2(vely, velx));
				cos = Math.cos(angle + ang45);
				sin = Math.sin(angle + ang45);
				angles.setHigh((float) (angle - 30.0f), (float) (angle + 30.0f));
				angles.setLow((float) angle);
			}
			lastchng += delta;
			if (lastchng > 0.2f) {
				zigzag *= -1;
				lastchng = 0;
			}
			setPos(posx, posy);
		}
		pe.update(delta);
	}
	public void update(float delta){
		pe.update(delta);
	}

	public void render(SpriteBatch batcher) {
		// pe.setEmittersCleanUpBlendFunction(true);
		pe.draw(batcher);
		if (pe.isComplete())
			pe.reset();

		// batcher.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void dispose() {
		// TODO Auto-generated method stub
		pe.dispose();
	}

	public void setPos(float x, float y) {
		pe.setPosition(x, y);
		posx = x;
		posy = y;
	}

	public void resetpos(float x, float y) {
		EveNo = Config.listTest;
		LWcase = Integer.parseInt(Config.listTest);
		switch (LWcase) {
		case 0:
			pe.load(Gdx.files.internal("particle/enemage.party"), Gdx.files.internal("particle"));
			break;
		case 1:
			pe.load(Gdx.files.internal("particle/design4.party"), Gdx.files.internal("particle"));
			break;
		case 2:
			pe.load(Gdx.files.internal("particle/design5.party"), Gdx.files.internal("particle"));
			break;
		case 3:
			pe.load(Gdx.files.internal("particle/mage.party"), Gdx.files.internal("particle"));
			break;
		case 4:
			pe.load(Gdx.files.internal("particle/chakra.party"), Gdx.files.internal("particle"));
			break;
		default:
			pe.load(Gdx.files.internal("particle/mage.party"), Gdx.files.internal("particle"));
		}
		pe.setPosition(x, y);
		pe.start();

	}
}*/