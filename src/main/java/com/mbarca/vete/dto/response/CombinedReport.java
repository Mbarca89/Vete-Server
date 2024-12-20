package com.mbarca.vete.dto.response;

import java.util.Date;
import java.util.List;

public class CombinedReport {

    private List<Sale> sales;
    private List<Order> orders;
    private List<Bill> bills;
    private List<Payment> payments;

    public CombinedReport(List<Sale> sales, List<Order> orders, List<Bill> bills, List<Payment> payments) {
        this.sales = sales;
        this.orders = orders;
        this.bills = bills;
        this.payments = payments;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "CombinedReport{" +
                "sales=" + sales +
                ", orders=" + orders +
                ", bills=" + bills +
                ", payments=" + payments +
                '}';
    }

    // Inner classes to model the data
    public static class Sale {
        private Date saleDate;
        private double saleAmount;
        private double saleCost;

        public Sale(Date saleDate, double saleAmount, double saleCost) {
            this.saleDate = saleDate;
            this.saleAmount = saleAmount;
            this.saleCost = saleCost;
        }

        public Date getSaleDate() {
            return saleDate;
        }

        public void setSaleDate(Date saleDate) {
            this.saleDate = saleDate;
        }

        public double getSaleAmount() {
            return saleAmount;
        }

        public void setSaleAmount(double saleAmount) {
            this.saleAmount = saleAmount;
        }

        public double getSaleCost() {
            return saleCost;
        }

        public void setSaleCost(double saleCost) {
            this.saleCost = saleCost;
        }

        @Override
        public String toString() {
            return "Sale{" +
                    "saleDate=" + saleDate +
                    ", saleAmount=" + saleAmount +
                    ", saleCost=" + saleCost +
                    '}';
        }
    }

    public static class Order {
        private Date orderDate;
        private double orderAmount;

        public Order(Date orderDate, double orderAmount) {
            this.orderDate = orderDate;
            this.orderAmount = orderAmount;
        }

        public Date getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(Date orderDate) {
            this.orderDate = orderDate;
        }

        public double getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(double orderAmount) {
            this.orderAmount = orderAmount;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderDate=" + orderDate +
                    ", orderAmount=" + orderAmount +
                    '}';
        }
    }

    public static class Bill {
        private Date billDate;
        private double totalAmount;

        public Bill(Date billDate, double totalAmount) {
            this.billDate = billDate;
            this.totalAmount = totalAmount;
        }

        public Date getBillDate() {
            return billDate;
        }

        public void setBillDate(Date billDate) {
            this.billDate = billDate;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        @Override
        public String toString() {
            return "Bill{" +
                    "billDate=" + billDate +
                    ", totalAmount=" + totalAmount +
                    '}';
        }
    }

    public static class Payment {
        private Date paymentDate;
        private double amount;

        public Payment(Date paymentDate, double amount) {
            this.paymentDate = paymentDate;
            this.amount = amount;
        }

        public Date getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(Date paymentDate) {
            this.paymentDate = paymentDate;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Payment{" +
                    "paymentDate=" + paymentDate +
                    ", amount=" + amount +
                    '}';
        }
    }
}
