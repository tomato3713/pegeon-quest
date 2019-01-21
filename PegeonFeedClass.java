// 鳩の餌のクラス
class PegeonFeedClass extends Figure {
    private int kind;
    PegeonFeedClass(int x, int y, int kind) {
        String feedName = "";
        this.setX(x); this.setY(y);
        this.kind = kind;
        if( kind == 1) feedName = "javaesa.jpg";
        if( kind == 2) feedName = "reportesa.jpg";
        if( kind == 3) feedName = "esa.jpg";

        this.setImg(feedName);
    }
}
