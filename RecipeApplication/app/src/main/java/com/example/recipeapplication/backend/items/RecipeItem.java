package com.example.recipeapplication.backend.items;

public class RecipeItem {
    private final String rec_nm;    // 메뉴명
    private final String rcp_way2;  // 조리방법
    private final String rcp_pat2;  // 요리종류
    private final String info_wgt;  // 중량(1인분)
    private final String info_eng;  // 열량
    private final String info_car;  // 탄수화물
    private final String info_pro;  // 단백질
    private final String info_fat;  // 지방
    private final String info_na;   // 나트륨
    private final String att_file_no_main;  // 이미지경로(소)
    private final String att_file_no_mk;    // 이미지경로(대)
    private final String rcp_parts_dtls;    // 재료정보
    private final String[] manual;         // 만드는법 배열
    private final String[] manual_img;     // 만드는법 이미지 배열

    public RecipeItem(String rec_nm, String rcp_way2, String rcp_pat2, String info_wgt, String info_eng, String info_car, String info_pro, String info_fat, String info_na, String att_file_no_main, String att_file_no_mk, String rcp_parts_dtls, String[] manual, String[] manual_img) {
        this.rec_nm = rec_nm;
        this.rcp_way2 = rcp_way2;
        this.rcp_pat2 = rcp_pat2;
        this.info_wgt = info_wgt;
        this.info_eng = info_eng;
        this.info_car = info_car;
        this.info_pro = info_pro;
        this.info_fat = info_fat;
        this.info_na = info_na;
        this.att_file_no_main = att_file_no_main;
        this.att_file_no_mk = att_file_no_mk;
        this.rcp_parts_dtls = rcp_parts_dtls;
        this.manual = manual;
        this.manual_img = manual_img;
    }

    public String getAtt_file_no_main() {
        return att_file_no_main;
    }

    public String getAtt_file_no_mk() {
        return att_file_no_mk;
    }

    public String getInfo_car() {
        return info_car;
    }

    public String getInfo_eng() {
        return info_eng;
    }

    public String getInfo_fat() {
        return info_fat;
    }

    public String getInfo_na() {
        return info_na;
    }

    public String getInfo_pro() {
        return info_pro;
    }

    public String getInfo_wgt() {
        return info_wgt;
    }

    public String getRcp_parts_dtls() {
        return rcp_parts_dtls;
    }

    public String getRcp_pat2() {
        return rcp_pat2;
    }

    public String getRcp_way2() {
        return rcp_way2;
    }

    public String getRec_nm() {
        return rec_nm;
    }

    public String[] getManual() {
        return manual;
    }

    public String[] getManual_img() {
        return manual_img;
    }
}
