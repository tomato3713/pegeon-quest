// 鳩の餌のクラス
class PegeonFeedClass extends Figure {
    private int kind;
    PegeonFeedClass(int x, int y, int kind) {
        String feedName = "";
        this.setX(x); this.setY(y);
        this.kind = kind;
        if( kind == 1) feedName = "feed_java.jpg";
        if( kind == 2) feedName = "feed_report.jpg";
        if( kind == 3) feedName = "feed_food.jpg";

        this.setImg(feedName);
    }
}
