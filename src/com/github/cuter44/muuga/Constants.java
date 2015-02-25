package com.github.cuter44.muuga;

public class Constants
{
  // ERROR HANDLING
    public static final String KEY_EXCEPTION = "exception";
    //public static final String KEY_RESULT = "result";
    public static final String URI_ERROR_HANDLER = "/sys/exception.api";

  // HTTP PARAM
    public static final String KEY_UID = "uid";

  // EVENT TYPE
    // USER
    public static final String EVENTTYPE_USER_REGISTER      = "user.register";
    public static final String EVENTTYPE_USER_ACTIVATE      = "user.activate";
    public static final String EVENTTYPE_USER_LOGIN         = "user.login";
    public static final String EVENTTYPE_USER_LOGOUT        = "user.logout";

    // BUDDY
    public static final String EVENTTYPE_BUDDY_FOLLOW       = "buddy.follow";
    public static final String EVENTTYPE_BUDDY_UNFOLLOW     = "buddy.unfollow";
    public static final String EVENTTYPE_BUDDY_HATE         = "buddy.hate";
    public static final String EVENTTYPE_BUDDY_UNHATE       = "buddy.unhate";

    public static final String EVENTTYPE_TRADE_BUYERINIT    = "trade.buyerinit";
    public static final String EVENTTYPE_TRADE_SELLERINIT   = "trade.sellerinit";
    public static final String EVENTTYPE_TRADE_BUYERACCEPT  = "trade.buyeraccept";
    public static final String EVENTTYPE_TRADE_SELLERACCEPT = "trade.selleraccept";
    public static final String EVENTTYPE_TRADE_CONSUMEQUIT  = "trade.consumeaccept";
    public static final String EVENTTYPE_TRADE_SUPPLYQUIT   = "trade.supplyaccept";
    public static final String EVENTTYPE_TRADE_DELIVERED    = "trade.delivered";
    public static final String EVENTTYPE_TRADE_FINISH       = "trade.finish";

    //public static final String EVENTTYPE_LOAN
}
