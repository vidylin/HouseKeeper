package com.hrsst.housekeeper.common.data;

/**
 * Created by dxs on 2015/9/9.
 */
public class Prepoint {
    public int id;
    public String nickName0="1";
    public String nickName1="2";
    public String nickName2="3";
    public String nickName3="4";
    public String nickName4="5";

    public Prepoint() {

    }

    public Prepoint(int id, String nickName0, String nickName1, String nickName2, String nickName3, String nickName4) {
        this.id = id;
        this.nickName0 = nickName0;
        this.nickName1 = nickName1;
        this.nickName2 = nickName2;
        this.nickName3 = nickName3;
        this.nickName4 = nickName4;
    }

    public String[] getNames(){
        return new String[]{nickName0,nickName1,nickName2,nickName3,nickName4};
    }

    public String getName(int prepoint){
        switch (prepoint){
            case 0:
                return nickName0;
            case 1:
                return nickName1;
            case 2:
                return nickName2;
            case 3:
                return nickName3;
            case 4:
                return nickName4;
            default:
                return String.valueOf(prepoint);

        }
    }

    public void setName(int prepoint,String nameNew){
        switch (prepoint){
            case 0:
                this.nickName0=nameNew;
                break;
            case 1:
                this.nickName1=nameNew;
                break;
            case 2:
                this.nickName2=nameNew;
                break;
            case 3:
                this.nickName3=nameNew;
                break;
            case 4:
                this.nickName4=nameNew;
                break;
            default:
                break;
        }
    }
}
