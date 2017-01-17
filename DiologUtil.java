
/**
 * Created by dmx on 2017/1/16.
 */

public class DialogUtil extends Dialog {
    private Context context;
    private TextView tv_message;
    private TextView tv_cancel, tv_ok;

    private String message;
    private String ok_text;
    private String cancel_text;

    private int messageColor;
    private int ok_text_color;
    private int cancel_text_color;


    /**
     * 构造函数
     *
     * @param context
     */
    public DialogUtil(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
        setContentView(R.layout.custom_dialog);

        initView();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        tv_message = (TextView) findViewById(R.id.custom_dialog_message);
        tv_cancel = (TextView) findViewById(R.id.custom_dialog_cancel);
        tv_ok = (TextView) findViewById(R.id.custom_dialog_ok);
    }


    public void setOk_text_color(int ok_text_color) {
        this.ok_text_color = ok_text_color;

        tv_ok.setTextColor(ok_text_color);
    }

    public void setCancel_text_color(int cancel_text_color) {
        this.cancel_text_color = cancel_text_color;

        tv_cancel.setTextColor(cancel_text_color);
    }

    public void setMessageColor(int messageColor) {
        this.messageColor = messageColor;

        tv_message.setTextColor(messageColor);
    }

    /**
     * 设置提示内容
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
        tv_message.setText(message);
    }

    /**
     * 设置右边按文字
     *
     * @param ok_text
     */
    public void setOk_text(String ok_text) {
        this.ok_text = ok_text;

        tv_ok.setText(ok_text);
    }

    /**
     * 设置左边文字
     *
     * @param cancel_text
     */
    public void setCancel_text(String cancel_text) {
        this.cancel_text = cancel_text;

        tv_cancel.setText(cancel_text);
    }

    /**
     * 为点击ok设置监听
     *
     * @param listener
     */
    public void setOkListener(View.OnClickListener listener) {
        tv_ok.setOnClickListener(listener);
    }

    /**
     * 设置取消监听
     *
     * @param listener
     */
    public void setCancelListener(View.OnClickListener listener) {
        tv_cancel.setOnClickListener(listener);
    }

    /**
     * 显示dialog
     */
    @Override
    public void show() {
        super.show();
    }


    /**
     * dialog取消
     */
    @Override
    public void cancel() {
        super.cancel();
        dismiss();
    }


    /**
     * 设置dialog 外部区域变暗
     */
    public void setBackgroundGray() {
        WindowManager.LayoutParams attributes = DialogUtil.this.getWindow().getAttributes();
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.dimAmount = 0.5f;
        DialogUtil.this.getWindow().setAttributes(attributes);
    }

    /**
     * 设置点击外部区域是否消失
     *
     * @param status
     */
    public void setCancelOnTouchOutside(boolean status) {
        DialogUtil.this.setCanceledOnTouchOutside(status);
    }

    /**
     *  调用示例
     *
     final DialogUtil dialogUtil = new DialogUtil(this);
     
     dialogUtil.setMessage("ssssssssssssss");
     dialogUtil.setOk_text("支付宝");
     dialogUtil.setCancel_text("微信");
     dialogUtil.setCancel_text_color(getResources().getColor(R.color.hintTextColor));

     dialogUtil.setOkListener(new View.OnClickListener() {
    @Override public void onClick(View view) {
    Toast.makeText(test.this, "sssssssssss", Toast.LENGTH_SHORT).show();
    }
    });

     dialogUtil.setCancelListener(new View.OnClickListener() {
    @Override public void onClick(View view) {
    dialogUtil.dismiss();
    }
    });

     dialogUtil.show();
     dialogUtil.setBackgroundGray();
     dialogUtil.setCancelOnTouchOutside(false);
     *
     * **/
}
