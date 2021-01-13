package com.test.sign;

public class nanowork {

    private static long blake2(long w0, long w1, long w2, long w3, long w4) {
        long t0 = 0x6a09e667f2bdc900L, t1 = 0xbb67ae8584caa73bL, t2 = 0x3c6ef372fe94f82bL, t3 = 0xa54ff53a5f1d36f1L;
        long t4 = 0x510e527fade682d1L, t5 = 0x9b05688c2b3e6c1fL, t6 = 0x1f83d9abfb41bd6bL, t7 = 0x5be0cd19137e2179L;
        long t8 = 0x6a09e667f3bcc908L, t9 = 0xbb67ae8584caa73bL, ta = 0x3c6ef372fe94f82bL, tb = 0xa54ff53a5f1d36f1L;
        long tc = 0x510e527fade682f9L, td = 0x9b05688c2b3e6c1fL, te = 0xe07c265404be4294L, tf = 0x5be0cd19137e2179L;
        t0 += t4+w0; tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4+w1; tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5+w2; td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5+w3; td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6+w4; te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6;    te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7;    tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5;    tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;    tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6;    tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7;    td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4;    te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4;    te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4;    tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5+w4; td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5;    td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6;    te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6;    te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7;    tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5+w1; tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;    tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6+w0; tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6+w2; tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7;    td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4;    te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4+w3; te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4;    tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5;    td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5+w0; td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6;    te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6+w2; te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7;    tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5;    tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;    tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6+w3; tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6;    tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7+w1; td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4;    te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4+w4; te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4;    tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5+w3; td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5+w1; td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6;    te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6;    te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7;    tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5+w2; tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;    tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6;    tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7+w4; td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7+w0; td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4;    te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4;    te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4+w0; tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5;    td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5;    td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6+w2; te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6+w4; te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7;    tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5;    tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5+w1; tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6;    tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7;    td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4+w3; te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4;    te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4+w2; tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4;    tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5;    td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5;    td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6+w0; te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6;    te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7;    tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7+w3; tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5+w4; tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;    tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6;    tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7;    td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4+w1; te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4;    te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4;    tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5+w1; td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5;    td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6;    te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6;    te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7+w4; tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5+w0; tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;    tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6+w3; tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7+w2; td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4;    te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4;    te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4;    tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5;    td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5;    td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6;    te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6+w1; te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7+w3; tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5;    tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5+w0; tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6+w4; tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7;    td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4+w2; te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4;    te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4;    tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5;    td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5;    td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6;    te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6+w3; te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7+w0; tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5;    tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5+w2; tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6;    tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7+w1; td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7+w4; td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4;    te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4;    te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4+w2; tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5;    td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5+w4; td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6;    te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6;    te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7+w1; tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5;    tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;    tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6;    tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7+w3; td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7;    td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4;    te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4+w0; te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4+w0; tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4+w1; tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc; t4 = (((t4^t8) << 1) | ((t4^t8) >>> 63)); t1 += t5+w2; td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5+w3; td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6+w4; te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6;    te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te; t6 = (((t6^ta) << 1) | ((t6^ta) >>> 63)); t3 += t7;    tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5;    tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;    tf = (((tf^t0) << 48) | ((tf^t0) >>> 16)); ta += tf; t5 = (((t5^ta) << 1) | ((t5^ta) >>> 63)); t1 += t6;    tc = (((tc^t1) << 32) | ((tc^t1) >>> 32)); tb += tc; t6 = (((t6^tb) << 40) | ((t6^tb) >>> 24)); t1 += t6;    tc = (((tc^t1) << 48) | ((tc^t1) >>> 16)); tb += tc; t6 = (((t6^tb) << 1) | ((t6^tb) >>> 63)); t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7;    td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td; t7 = (((t7^t8) << 1) | ((t7^t8) >>> 63)); t3 += t4;    te = (((te^t3) << 32) | ((te^t3) >>> 32)); t9 += te; t4 = (((t4^t9) << 40) | ((t4^t9) >>> 24)); t3 += t4;    te = (((te^t3) << 48) | ((te^t3) >>> 16)); t9 += te; t4 = (((t4^t9) << 1) | ((t4^t9) >>> 63));
        t0 += t4;    tc = (((tc^t0) << 32) | ((tc^t0) >>> 32)); t8 += tc; t4 = (((t4^t8) << 40) | ((t4^t8) >>> 24)); t0 += t4;    tc = (((tc^t0) << 48) | ((tc^t0) >>> 16)); t8 += tc;                                           t1 += t5+w4; td = (((td^t1) << 32) | ((td^t1) >>> 32)); t9 += td; t5 = (((t5^t9) << 40) | ((t5^t9) >>> 24)); t1 += t5;    td = (((td^t1) << 48) | ((td^t1) >>> 16)); t9 += td; t5 = (((t5^t9) << 1) | ((t5^t9) >>> 63)); t2 += t6;    te = (((te^t2) << 32) | ((te^t2) >>> 32)); ta += te; t6 = (((t6^ta) << 40) | ((t6^ta) >>> 24)); t2 += t6;    te = (((te^t2) << 48) | ((te^t2) >>> 16)); ta += te;                                           t3 += t7;    tf = (((tf^t3) << 32) | ((tf^t3) >>> 32)); tb += tf; t7 = (((t7^tb) << 40) | ((t7^tb) >>> 24)); t3 += t7;    tf = (((tf^t3) << 48) | ((tf^t3) >>> 16)); tb += tf; t7 = (((t7^tb) << 1) | ((t7^tb) >>> 63)); t0 += t5+w1; tf = (((tf^t0) << 32) | ((tf^t0) >>> 32)); ta += tf; t5 = (((t5^ta) << 40) | ((t5^ta) >>> 24)); t0 += t5;                                                                                                                                                                                                                                                                                                                            t2 += t7;    td = (((td^t2) << 32) | ((td^t2) >>> 32)); t8 += td; t7 = (((t7^t8) << 40) | ((t7^t8) >>> 24)); t2 += t7;    td = (((td^t2) << 48) | ((td^t2) >>> 16)); t8 += td;
        return t0 ^ t8 ^ 0x6a09e667f2bdc900L;
    }

    private static long nanowork(long w0, long w1, long w2, long w3)
    {
        long h, work = 0;
        do h = blake2(++work, w0, w1, w2, w3); while (h >= 0 || h <= 0xffffffc000000000L);
        return work;
    }

    private static long parse64(String s)
    {
        long w0 = Long.parseLong(s.substring(0, 8), 16);
        long w1 = Long.parseLong(s.substring(8, 16), 16);
        return Long.reverseBytes(w0 << 32 | w1);
    }

    public static void main(String[] args)
    {
        if (args.length != 1) System.exit(-1);
        String arg = args[0];
        if (arg.length() != 64) System.exit(-1);
        if (!arg.matches("^[0-9A-Fa-f]+$")) System.exit(-1);
        long w0 = parse64(arg.substring( 0, 16));
        long w1 = parse64(arg.substring(16, 32));
        long w2 = parse64(arg.substring(32, 48));
        long w3 = parse64(arg.substring(48, 64));
        long work = nanowork(w0, w1, w2, w3);
        System.out.println(String.format("%016x", work));
    }

}
