package net.atommobile.pemudakahfi.ext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by faizlidwibrido on 9/18/16.
 */
public class AdaptGridView extends GridView {

    public AdaptGridView(Context context) {
        super(context);
    }

    public AdaptGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}