package BankingApp.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import BankingApp.entity.Account;
import BankingApp.entity.Transfer;
import BankingApp.entity.TransferDate;
import BankingApp.repository.TransferDateRepository;
import BankingApp.repository.TransferRepository;
import org.springframework.stereotype.Service;


@Service
public class TransferAndTransferDateService {

    private final TransferDateRepository transferDateRepository;

    private final TransferRepository transferRepository;

    public TransferAndTransferDateService(TransferDateRepository transferDateRepository,
                                          TransferRepository transferRepository) {
        this.transferDateRepository = transferDateRepository;
        this.transferRepository = transferRepository;
    }

    private List<TransferDate> getAllTransferDates () {return transferDateRepository.findAll();}

    public TransferDate addNewTransferDateIfNotExist () {
        List<TransferDate> listOfTransferDates = getAllTransferDates();
        TransferDate transferDateNew =null;
        String transferDate = transferDate();

        if(listOfTransferDates.size() !=0) {
            for (int i = 0; i < listOfTransferDates.size(); i++) {
                if (Objects.equals(listOfTransferDates.get(i).getTransferDate(), transferDate)) {
                    transferDateNew = listOfTransferDates.get(i);
                    break;
                } else if (i == listOfTransferDates.size() - 1) {
                    transferDateNew = new TransferDate(transferDate);
                }
            }
        } else {
            transferDateNew = new TransferDate(transferDate);
        }

        return transferDateNew;
    }

    private void updateTransferListToDate (Transfer transfer, TransferDate transferDateNew) {
        List<Transfer> newTransferList = new ArrayList<>();
        if (transferDateNew.getTransferListToDate() != null) {
            newTransferList = transferDateNew.getTransferListToDate();
        }
        newTransferList.add(transfer);
        transferDateNew.setTransferListToDate(newTransferList);

    }

    public Transfer createAndSaveNewTransfer (String accountNumber, String transferAccountNumber, String transferMoney,
                                              Account account, Account transferAccount, String accountInfo ) {

        TransferDate transferDateNew = addNewTransferDateIfNotExist();

        Transfer transfer = new Transfer(transferAccountNumber,transferMoney,transferDateNew, account);
        transfer.setAccountInfo(transferAccount.getUser().getUsername());
//        if (accountNumber.equals(transferAccountNumber)) {
//            transfer.setAccountInfo(accountInfo);
//        } else {
//            transfer.setAccountInfo(transferAccount.getUser().getUsername());
//        }

        updateTransferListToDate(transfer,transferDateNew);
        transferDateRepository.save(transferDateNew);
        transferRepository.save(transfer);

        return transfer;
    }


    private String transferDate () {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MMMM");
        return localDate.format(formatter);

    }

}
