package com.mygdx.purefaithstudio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ParticleLayer {
    /*String vertexShader;
    String fragmentShader;
    ShaderProgram shaderProgram;*/
	public static String EveNo = "0";
	public static int LWcase = 0;
	public final String flame = "particle/enemage.party";
	public final String blueflame = "particle/mage.party";
	public final String blueflame2 = "particle/mage2.party";
	//public final String smdesign = "particle/design4.party";
	//public final String lgdesign = "particle/design5.party";
	public final String chakra = "particle/chakra.party";
	public final String sparks = "particle/sparks.party";
	public final String sparks2 = "particle/sparks2.party";
	public final String frag = "particle/frag.party";
	public final String snow = "particle/snow.party";
    public final String leaf = "particle/leaf.party";
	public AssetManager assetm;
	Array<PooledEffect> effects;
	ParticleEffect pe;
	ParticleEffectPool PePool[];
	PooledEffect pooleff;
	ParticleEffectParameter pep;
	ScaledNumericValue angle;
	private float lastchange = 0, nxtchng = 5,currentAngle=0,changeAngle=0,variation;
	private float tint[];
	private boolean upscale = true;

	public ParticleLayer() {
		pep = new ParticleEffectParameter();
		effects = new Array<PooledEffect>();
        /*vertexShader = Gdx.files.internal("shaders/ripple.vert").readString();
        fragmentShader = Gdx.files.internal("shaders/ripple.frag").readString();
        shaderProgram = new ShaderProgram(vertexShader,fragmentShader);
        ShaderProgram.pedantic = false;
        shaderProgram.begin();
       shaderProgram.setUniformf("u_time",0.1f);
        //shaderProgram.setUniformi("u_texture", 0);
        shaderProgram.setUniformf("u_resolution", new Vector2(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        shaderProgram.end();*/
	}

	public void loadeffect() {
        dispose();
        assetm = new AssetManager();
		EveNo = Config.listTest;
		LWcase = Integer.parseInt(EveNo);
		effects.clear();
		switch (LWcase) {

            case 0:
                pep.atlasFile = "particle/pack.atlas";
                assetm.load(blueflame, ParticleEffect.class, pep);
                assetm.finishLoading();
                pe = assetm.get(blueflame, ParticleEffect.class);
                PePool = new ParticleEffectPool[1];
                PePool[0] = new ParticleEffectPool(pe, 1, 5);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(150, 610);
                pooleff.scaleEffect(1.2f);
                setAngle(pooleff, 250, 10, 60);
                effects.add(pooleff);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(350, 610);
                pooleff.scaleEffect(1.2f);
                setAngle(pooleff, 280, 50, 60);
                effects.add(pooleff);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(80, 350);
                pooleff.scaleEffect(0.8f);
                setAngle(pooleff, 270, 0, 60);
                effects.add(pooleff);
                break;

            case 1:
                pep.atlasFile = "particle/pack.atlas";
                assetm.load(flame, ParticleEffect.class, pep);
                assetm.load(sparks, ParticleEffect.class, pep);
                assetm.finishLoading();
                PePool = new ParticleEffectPool[2];
                pe = assetm.get(flame, ParticleEffect.class);
                PePool[0] = new ParticleEffectPool(pe, 1, 5);
                pe = assetm.get(sparks, ParticleEffect.class);
                PePool[1] = new ParticleEffectPool(pe, 1, 5);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(295, 650);
                pooleff.scaleEffect(1.3f);
                setAngle(pooleff, 270, 60, 100);
                effects.add(pooleff);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(35, 270);
                pooleff.scaleEffect(2f);
                setAngle(pooleff, 270, 40,80);
                effects.add(pooleff);
                pooleff = PePool[1].obtain();
                pooleff.setPosition(240, 20);
                //pooleff.scaleEffect(1f);
                effects.add(pooleff);
                break;

            case 2:
                pep.atlasFile = "particle/pack.atlas";
                assetm.load(blueflame2, ParticleEffect.class, pep);
                assetm.finishLoading();
                PePool = new ParticleEffectPool[1];
                pe = assetm.get(blueflame2, ParticleEffect.class);
                PePool[0] = new ParticleEffectPool(pe, 1, 5);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(300, 400);
                pooleff.scaleEffect(1.6f);
                setAngle(pooleff, 270, 20, 40);
                effects.add(pooleff);
                break;

            case 3:
                pep.atlasFile = "particle/pack.atlas";
                assetm.load(sparks2, ParticleEffect.class, pep);
                assetm.finishLoading();
                PePool = new ParticleEffectPool[1];
                pe = assetm.get(sparks2, ParticleEffect.class);
                PePool[0] = new ParticleEffectPool(pe, 1, 5);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(240, 0);
                pooleff.scaleEffect(1.5f);
                effects.add(pooleff);
                break;

            case 4:
                pep.atlasFile = "particle/pack.atlas";
                assetm.load(sparks2, ParticleEffect.class, pep);
                assetm.finishLoading();
                PePool = new ParticleEffectPool[1];
                pe = assetm.get(sparks2, ParticleEffect.class);
                PePool[0] = new ParticleEffectPool(pe, 1, 5);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(240, 720);
                pooleff.scaleEffect(-2f);
                effects.add(pooleff);
                break;

            case 5:
                pep.atlasFile = "particle/pack.atlas";
                assetm.load(frag, ParticleEffect.class, pep);
                assetm.finishLoading();
                PePool = new ParticleEffectPool[1];
                pe = assetm.get(frag, ParticleEffect.class);
                PePool[0] = new ParticleEffectPool(pe, 1, 5);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(240, 20);
                pooleff.scaleEffect(2f);
                effects.add(pooleff);
                break;

            case 6:
                pep.atlasFile = "particle/pack.atlas";
                assetm.load(snow, ParticleEffect.class, pep);
                assetm.finishLoading();
                PePool = new ParticleEffectPool[1];
                pe = assetm.get(snow, ParticleEffect.class);
                PePool[0] = new ParticleEffectPool(pe, 1, 5);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(240, 800);
                pooleff.scaleEffect(2.2f);
                effects.add(pooleff);
                break;

            case 7:
                pep.atlasFile = "particle/pack.atlas";
                assetm.load(snow, ParticleEffect.class, pep);
                assetm.finishLoading();
                PePool = new ParticleEffectPool[1];
                pe = assetm.get(snow, ParticleEffect.class);
                PePool[0] = new ParticleEffectPool(pe, 1, 5);
                pooleff = PePool[0].obtain();
                pooleff.setPosition(240, 800);
                pooleff.scaleEffect(2.2f);
                effects.add(pooleff);
                break;

            case 8:
                break;

            case 9:
                break;

            case 10:
                break;

            case 11:
                break;

            case 12:
                break;

            case 13:
                break;

            case 14:
                break;

            case 15:
                break;
			
		default:
            pep.atlasFile = "particle/pack.atlas";
            assetm.load(sparks2, ParticleEffect.class, pep);
            assetm.finishLoading();
            PePool = new ParticleEffectPool[1];
            pe = assetm.get(sparks2, ParticleEffect.class);
            PePool[0] = new ParticleEffectPool(pe, 1, 5);
            pooleff = PePool[0].obtain();
            pooleff.setPosition(240, 0);
            pooleff.scaleEffect(1.5f);
            effects.add(pooleff);
            break;
		}
		if(pooleff !=null)
		    pooleff.dispose();
	}

	public void update(float delta) {
        pulse(delta,0.5f);
        /*if(LWcase == 7){
        	rotateChakra(delta);
        }*/
		for (PooledEffect poole : effects) {
			poole.update(delta);
		}
	}
	
	public void rotateChakra(float delta){
		changeAngle+=delta;
		if(changeAngle>0.1f){
			currentAngle = currentAngle>350 ? 10: currentAngle+10;
			changeAngle = 0;
		for(PooledEffect poole:effects){
			for(int i=0;i<poole.getEmitters().size;i++){
				variation = currentAngle + i*120;
				angle = poole.getEmitters().get(i).getAngle();
				angle.setLow(variation);
				angle.setHigh(variation - 30, variation + 30);
				
			}
		}
		}
	}

	public void pulse(float delta,float reverseScale){
		if (Config.moving) {
			lastchange += delta;
			if (lastchange > nxtchng) {
				lastchange = 0;
				if (upscale) {
					setScale(1/reverseScale);
					upscale = false;
					nxtchng = 0.1f;
				} else {
					setScale(reverseScale);
					upscale = true;
					nxtchng = 3;
				}
			}
		}

	}

	public void setColor(float[] colors) {
		for (PooledEffect poole : effects) {
			for (ParticleEmitter em : poole.getEmitters()) {
				tint=em.getTint().getColors();
				tint[0]=colors[0];
				tint[1]=colors[1];
				tint[2]=colors[2];
			}
		}
	}
	
	public void setEffectColor(PooledEffect poole,float[] colors) {
			for (ParticleEmitter em : poole.getEmitters()) {
				tint=em.getTint().getColors();
				 tint[0] = 0.337f;
				 tint[1] = 0.325f;
				 tint[2] = 0.914f;
			}
	}

	public void setScale(float scale) {
		for (PooledEffect poole : effects) {
			poole.scaleEffect(scale);
		}
	}

	public void setWind(float speed) {
		for (PooledEffect poole : effects) {
			for (ParticleEmitter em : poole.getEmitters()) {
				if (speed != 0) {
					em.getWind().setHigh(speed);
					em.getWind().setActive(true);
				} else {
					em.getWind().setActive(false);
				}
			}
		}
	}

	public void setAngle(PooledEffect effect, float angleInput, float variation, float deviation) {
		for (ParticleEmitter em : effect.getEmitters()) {
			angle = em.getAngle();
			angle.setLow(angleInput);
			angle.setHigh(angleInput - variation, angleInput + deviation - variation);
		}
	}

	public void render(SpriteBatch batcher) {

		for (PooledEffect poole : effects) {
			poole.draw(batcher);
			if (poole.isComplete()) {
				poole.reset();
			}
		}
	}

	public void dispose() {

		for (PooledEffect poole : effects) {
			poole.dispose();
		}
        if(PePool !=null)
            for(ParticleEffectPool pepo: PePool) {
                pepo.clear();
            }
        if(assetm != null)
            assetm.dispose();
        if(pe !=null)
            pe.dispose();
        effects.clear();
	}

	public void setPos(float x, float y, int index) {
		effects.get(index).setPosition(x, y);
	}

}
