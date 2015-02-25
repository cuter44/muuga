package com.github.cuter44.muuga.user.daemon;

import com.github.cuter44.nyafx.event.*;
import com.alibaba.fastjson.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.contract.servlet.Json;
import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.easemob.core.*;


public class ContractNotifyDaemon
    implements Runnable
{
    protected Easemob easemob;
    protected EventQueue<ContractBase> eq;

    public ContractNotifyDaemon()
    {
        this.easemob = Easemob.getInstance();
        this.eq = new EventQueue<ContractBase>();

        EventHub evHub = EventHub.getInstance();
        evHub.subscribe(EVENTTYPE_TRADE_BUYERINIT    , this.eq);
        evHub.subscribe(EVENTTYPE_TRADE_SELLERINIT   , this.eq);
        evHub.subscribe(EVENTTYPE_TRADE_BUYERACCEPT  , this.eq);
        evHub.subscribe(EVENTTYPE_TRADE_SELLERACCEPT , this.eq);
        evHub.subscribe(EVENTTYPE_TRADE_CONSUMEQUIT  , this.eq);
        evHub.subscribe(EVENTTYPE_TRADE_SUPPLYQUIT   , this.eq);
        evHub.subscribe(EVENTTYPE_TRADE_DELIVERED    , this.eq);
        evHub.subscribe(EVENTTYPE_TRADE_FINISH       , this.eq);

        return;
    }

    public void run()
    {

        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                Event<ContractBase> ev = this.eq.take();
                ContractBase contract = ev.getContext();

                JSONObject json = Json.jsonizeContractBase(ev.getContext());
                String supplyId = contract.getSupply().getId().toString();
                String consumeId = contract.getConsume().getId().toString();

                this.easemob.postMsgCmd(json, supplyId, consumeId);

            }
            catch (Exception ex)
            {
                if (!(ex instanceof InterruptedException))
                    ex.printStackTrace();
            }
        }
    }
}
