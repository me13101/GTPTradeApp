package com.gtp.tradeapp.service.gamification;

import com.gtp.tradeapp.entity.Gamer;
import com.gtp.tradeapp.entity.Position;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.repository.GamerRepository;
import com.gtp.tradeapp.repository.PositionRepository;
import com.gtp.tradeapp.repository.TransactionRepository;
import com.gtp.tradeapp.repository.UserRepository;
import com.gtp.tradeapp.service.marketdata.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GamerServiceImpl implements GamerService {

    private final GamerRepository gamerRepo;
    private final TransactionRepository txnRepo;
    private final UserRepository userRepo;

    private final PositionRepository posRepo;
    private final MarketDataService mktDataSvc;


    @Autowired
    public GamerServiceImpl(GamerRepository gamerRepo, TransactionRepository txnRepo, UserRepository userRepo, PositionRepository posRepo, MarketDataService mktDataSvc) {
        this.gamerRepo = gamerRepo;
        this.txnRepo = txnRepo;
        this.userRepo = userRepo;
        this.posRepo = posRepo;
        this.mktDataSvc = mktDataSvc;
    }

    @Override
    public Optional<Gamer> getGamerByUser(Long id) {
        Optional<Gamer> og = gamerRepo.findByUserIdIs(id);
        if (og.isPresent()) {
            return og;
        } else {
            Gamer gg = new Gamer(id);
            gamerRepo.save(gg);
            return Optional.of(gg);
        }
    }

    @Override
    public int updateGamer(Gamer gg, String col, int val) throws Exception {
        System.out.println("i rananrnarnarnanr");
        switch (col) {
            case "chips":
                int chips = gg.getChips();
                if ((chips + val) > 0) {
                    gg.setChips(chips += val);
                    gamerRepo.save(gg);
                    return 1;
                } else {
                    throw new Exception("Error in Updating Gamer Profile - Chips will go below 0");
                }
            case "exp":
                int exp = gg.getExp();
                exp += val;
                gg.setExp(exp);
                gamerRepo.save(gg);
                return 1;
        }

        throw new Exception("Error in Updating Gamer Profile");
    }

    @Override
    public List<User> getLeaderboard() {
        List<User> lb = (List<User>) userRepo.getLeaderboard();
        return lb.subList(0, 5);
    }

    @Override
    public List<String[]> getNavLeaderboard() {
        ArrayList<User> lbNav = (ArrayList<User>) userRepo.findAll();

        for (User currentUser : lbNav) {
            Optional<List<Position>> optCurrentPos = posRepo.findStockByUserId(currentUser.getId());
            BigDecimal currentPortfolio = new BigDecimal(0);
            if (optCurrentPos.isPresent()) {
                List<Position> currentPos = optCurrentPos.get();
                for (Position cPos : currentPos) {
                    if (cPos.getQuantity() != 0) {
                        currentPortfolio = currentPortfolio.add(mktDataSvc.getLatestStockPrice(cPos.getStockId()).getStocklist().get(0).getPrice().multiply(new BigDecimal(cPos.getQuantity()))).setScale(2, RoundingMode.CEILING);

                    }
                }
            }
            currentPortfolio = currentUser.getCash().add(currentPortfolio).setScale(2, RoundingMode.CEILING);
            System.out.println(currentPortfolio);
            currentUser.setCash(currentPortfolio);
            lbNav.set(lbNav.indexOf(currentUser), currentUser);
        }
        Collections.sort(lbNav);
        List<String[]> topFive = new ArrayList<String[]>();
        for (User u : lbNav.subList(0, 5)) {
            String[] o = {u.getFirstname() + u.getLastname(), u.getCash().toString(), u.getId().toString()};
            topFive.add(o);
        }
        return topFive;
    }


}