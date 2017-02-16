package schilkroete.master3;

// Instanzen dieser Klasse können die Daten eines SQLite-Datensatzes aufnehmen.
// Sie repräsentieren die Datensätze im Code.
// Ich werde mit Objekten dieser Klasse den ListView füllen

/*
* Später wird die ShoppingMemo-Klasse genutzen, um die Daten der Datensätze
* der SQLite-Datenbank in Java-Objekten zu speichern.
* Diese Objekte entsprechen dann exakt einem Datensatz.
* Mit den Objekten im Code kann dann weitergearbeitet werden und
* ihr Inhalt kann bequem in einer Liste, bspw. mit Hilfe eines ListViews, ausgeben werden.
*/
public class ShoppingMemo { ////Patientenakte

    private String product;
    private int quantity;
    private long id;

    public ShoppingMemo(String product, int quantity, long id) {
        this.product = product;
        this.quantity = quantity;
        this.id = id;
    }

    // Konstruktoren
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }


    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        String output = quantity + " x " + product;

        return output;
    }
}