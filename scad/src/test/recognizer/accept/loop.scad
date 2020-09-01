int a = 0;
int b;
int c = 0;
for (int i = 0; i < 10; i++) {
    a++;
    b = a;
    while (b >= i) {
        b--;
        do {
            c++;
        } while (c < b);
    }
}