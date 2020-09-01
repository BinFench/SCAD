Triangle {
    Array<Int points;

    Triangle Triangle(Shape a, Shape b, Shape c) {
        points.append(a);
        points.append(b);
        points.append(c);
        
        Shape triangle = Face(a, b, c);
    }
}

donut {
    int test;
}

Triangle test = Triangle(Point(0,0,0), Point(1,0,0), Point(0,1,0));
Triangle test2 = Triangle(Point(1x), Point(1y), Point(1z));