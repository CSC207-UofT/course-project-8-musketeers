#version 330
out vec4 fragColor;
uniform float xpos;
uniform float ypos;
uniform float vscale;

float mandel (float cx, float cy) {
	float x = 0;
	float y = 0;
	int i;
	for (i = 0; i < 16; i++) {
		if ((x*x + y*y) > 4) {
		  break;
		}
		float xtemp = x*x - y*y + cx;
		y = 2*x*y + cy;
		x = xtemp;
	}
	return 1 - (i / 16.f);
}

vec3 viewVec(float a,float b) {
    return vec3(sin(a) * cos(b), -sin(b), cos(a) * cos(b));
}
vec3 vVvert(float a,float b) {
   float b2 = b - 3.1415 / 2;
   return vec3(sin(a) * cos(b2), -sin(b2), cos(a) * cos(b2));
}
vec3 vVhorz(float a,float b) {
    float a2 = a + 3.1415 / 2;
    return vec3(sin(a2), 0, cos(a2));
}

float func(float x,float y) {
    //return sin(x*x+y*y);
    return min(2*mandel(x/3,y/3)-1, sin(sqrt(x*x+y*y)*4)) / 2;
    //return cos(x*2 + y*2) + sin(x*2 * y*2);
}

void main() {
  vec2 tc = gl_FragCoord.xy;
  vec2 wh = 1 / vec2(800, 800);
  vec2 cp = (tc*wh) - 0.5f;

  vec3 vv = viewVec(xpos, ypos);
  vec3 vx = vVvert(xpos, ypos);
  vec3 vy = vVhorz(xpos, ypos);

  vec3 ro = -8.0 * vv;

  vec3 pos = ro;

  vec3 rd = vv + cp.x * vx + cp.y * vy;

  bool hit = false;

  for (int i = 0; i < 256; i++) {
    float val = func(pos.x,pos.y);
    if (val <= pos.z) {
        hit = true;
        break;
    }
    pos = pos + 0.05 * rd;
  }
  if (hit) {
    fragColor = vec4(1 - (pos.z + 0.5));
  } else {
    fragColor = vec4(0.0,0.1,0.2,1.0);
  }

  //fragColor = vec4(m, m, m, 1.0);
  //fragColor = vec4((tc*wh).x, (tc*wh).y, 0.6, 1.0);
}