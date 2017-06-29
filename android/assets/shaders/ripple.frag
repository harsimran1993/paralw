#ifdef GL_ES
precision mediump float;
#endif 

// varying input variables from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoords;

// a special uniform for textures 
uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform vec3 overlayColor;
uniform vec2 u_mouse;
uniform float u_time;


void main()
{
	// set the color for this fragment|pixel
	//gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	vec2 uv = gl_FragCoord.xy / u_resolution.xy;

		if (uv.y > 0.25)// is air - no reflection or effect
		{
    		gl_FragColor =v_color * texture2D(u_texture, v_texCoords);//vec2(uv.x, uv.y));
		}
		else
		{
        
    	// Compute the mirror effect.
    	vec4 color = texture2D(u_texture,vec2(uv.x, 0.5 - uv.y));
    	vec4 finalColor = vec4(mix(color.rgb, overlayColor, 0.25), 1.0);
    	gl_FragColor = v_color * finalColor;
		}
}