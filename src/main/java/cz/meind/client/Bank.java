package cz.meind.client;

import java.math.BigInteger;

public class Bank implements Comparable<Bank> {
    private final String code;
    private final BigInteger total;
    private final Integer numberOfClients;
    private final BigInteger coefficient;

    public Bank(String code, BigInteger total, Integer numberOfClients) {
        this.code = code;
        this.total = total;
        this.numberOfClients = numberOfClients;
        this.coefficient = total.divide(BigInteger.valueOf(numberOfClients));

    }

    public String getCode() {
        return code;
    }

    public BigInteger getTotal() {
        return total;
    }

    public Integer getNumberOfClients() {
        return numberOfClients;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    @Override
    public int compareTo(Bank o) {
        return this.coefficient.compareTo(o.getCoefficient());
    }
}
