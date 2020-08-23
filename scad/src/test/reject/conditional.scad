int abs(int a) {
    int ret = a;
    if (a = 0) {
        ret *= -1;
    }
    return ret;
}

int test = abs(-7);

if (test == 0) {
    test += 1;
} else if (test < 3) {
    test *= 2;
} else if (test <= 6) {
    test /= 3;
} else {
    test--;
}