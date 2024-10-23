package demo.HotelManagement.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "transaction_types")
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private TransactionTypeUnit unit;

    private String typeGroup;
    private String typeSubGroup;

    @OneToMany(mappedBy = "transactionType", fetch = FetchType.EAGER)
    private List<Transaction> transactionList;
    public TransactionType() {}

    public TransactionType(Long id, String code, String name, String typeGroup) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.typeGroup = typeGroup;

    }

    public TransactionTypeUnit getUnit() {
        return unit;
    }

    public void setUnit(TransactionTypeUnit unit) {
        this.unit = unit;
    }

    public String getTypeSubGroup() {
        return typeSubGroup;
    }

    public void setTypeSubGroup(String typeSubGroup) {
        this.typeSubGroup = typeSubGroup;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }
}

