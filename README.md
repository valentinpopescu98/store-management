For H2 Console:
 - add empty file in user directory named: `test.mv.db`
 - add in Security Filter Chain `.headers(h -> h.frameOptions(f -> f.sameOrigin()))` to allow iFrames (H2 )