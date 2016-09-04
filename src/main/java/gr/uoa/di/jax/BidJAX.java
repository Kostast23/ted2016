package gr.uoa.di.jax;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bidder",
    "time",
    "amount"
})
@XmlRootElement(name = "Bid")
public class BidJAX {

    @XmlElement(name = "Bidder", required = true)
    protected BidderJAX bidder;
    @XmlElement(name = "Time", required = true)
    protected String time;
    @XmlElement(name = "Amount", required = true)
    protected String amount;

    /**
     * Gets the value of the bidder property.
     * 
     * @return
     *     possible object is
     *     {@link BidderJAX }
     *     
     */
    public BidderJAX getBidder() {
        return bidder;
    }

    /**
     * Sets the value of the bidder property.
     * 
     * @param value
     *     allowed object is
     *     {@link BidderJAX }
     *     
     */
    public void setBidder(BidderJAX value) {
        this.bidder = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTime(String value) {
        this.time = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

}
