package gr.uoa.di.jax;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gr.uoa.di.jax
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BidderJAX }
     * 
     */
    public BidderJAX createBidder() {
        return new BidderJAX();
    }

    /**
     * Create an instance of {@link LocationJAX }
     * 
     */
    public LocationJAX createLocation() {
        return new LocationJAX();
    }

    /**
     * Create an instance of {@link CategoryJAX }
     * 
     */
    public CategoryJAX createCategory() {
        return new CategoryJAX();
    }

    /**
     * Create an instance of {@link BidsJAX }
     * 
     */
    public BidsJAX createBids() {
        return new BidsJAX();
    }

    /**
     * Create an instance of {@link BidJAX }
     * 
     */
    public BidJAX createBid() {
        return new BidJAX();
    }

    /**
     * Create an instance of {@link ItemJAX }
     * 
     */
    public ItemJAX createItem() {
        return new ItemJAX();
    }

    /**
     * Create an instance of {@link SellerJAX }
     * 
     */
    public SellerJAX createSeller() {
        return new SellerJAX();
    }

    /**
     * Create an instance of {@link ItemsJAX }
     * 
     */
    public ItemsJAX createItems() {
        return new ItemsJAX();
    }

}
