package com.eli.bettermb.cli;

import com.eli.bettermb.client.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.ConnectException;

class QuotaTest
{
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream printStream;
    CLI cli;
    CustomStubHttpClient stub;
    QuotaSummary quota;
    SimpleResponse simple;
    @BeforeEach
    void setup()
    {
        cli = new CLI(System.in, new PrintStream(out, true));

        cli.client = new Client();
        cli.client.config = Configuration.test;

        stub = new CustomStubHttpClient();
        quota = new QuotaSummary();
        stub.shouldThrowException = false;
    };
    @Test
    void testQuotaIncrease()
    {
        String responseString = """
        { success: true, message: null }
        """
        stub.setResponseFromString(200, responseString);
        cli.client.ihttpClient = stub;

        String[] args = {"quota","+100"};
        assertDoesNotThrow(() -> { simple = cli.account(args); });

        System.out.println("Output captured: |" + out.toString() + "|");
        assertNotEquals(out.size(), 0);
    };
}
