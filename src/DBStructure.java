public class DBStructure {
    public static final String ORDER_SLIPS_MAIN="order_slips_main";




    public static final String OSM_ID="slip_id";
    public static final String OSM_CUSTOMER_NAME="customer_name";

    public int getIDsOrderSlipsMain(){
        String sql="select "+OSM_CUSTOMER_NAME+" from "+ORDER_SLIPS_MAIN+" where "+OSM_CUSTOMER_NAME+"=?";
        return -1;
    }
}
