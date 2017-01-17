# Utils
DiologUtil.java
封装了一个自定义dialog
1,可以自定义字体颜色
2,可以自定义文字内容，包括提示消息，取消按钮，确认按钮
3,可以设置背景为暗色
4,可以设置点击外部区域不消失

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
