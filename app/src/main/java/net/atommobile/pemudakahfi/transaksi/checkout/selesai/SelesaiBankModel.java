package net.atommobile.pemudakahfi.transaksi.checkout.selesai;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class SelesaiBankModel {

    private String id;
    private String bank;
    private String norek;
    private String atasnama;

    public SelesaiBankModel(String id, String bank, String norek, String atasnama){
        this.id = id;
        this.bank = bank;
        this.norek = norek;
        this.atasnama = atasnama;
    }

    public void setAtasnama(String atasnama) {
        this.atasnama = atasnama;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getAtasnama() {
        return atasnama;
    }

    public String getBank() {
        return bank;
    }

    public String getId() {
        return id;
    }

    public String getNorek() {
        return norek;
    }
}

