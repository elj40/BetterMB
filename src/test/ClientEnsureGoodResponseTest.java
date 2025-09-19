import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;


class ClientEnsureGoodResponseTest
{
    Client client;
    CustomStubHttpClient stub;
    @BeforeEach
    void setup()
    {
        client = new Client();
        stub = new CustomStubHttpClient();
        Client.debugging = false;
        client.setHttpClient(stub);
    };
    //@Test
    //void testSuccess()
    //{
    //    stub.setResponseFromString(200, "");
    //    assertDoesNotThrow(() -> client.ensureGoodResponse(stub));
    //};
    //@Test
    //void testFailStatusCode()
    //{
    //    stub.setResponseFromString(201, "");
    //    assertThrows(new IOException(), () -> client.ensureGoodResponse(stub));
    //    stub.setResponseFromString(199, "");
    //    assertThrows(new IOException(), () -> client.ensureGoodResponse(stub));
    //    stub.setResponseFromString(403, "");
    //    assertThrows(new IOException(), () -> client.ensureGoodResponse(stub));
    //    stub.setResponseFromString(500, "");
    //    assertThrows(new SIOException(), () -> client.ensureGoodResponse(stub));
    //};
    //@Test
    //void testFailUnexpectedHTML()
    //{
    //    stub.setResponseFromString(200, "<!DOCTYPE html><html><body>Hello!</body></html>");
    //    assertThrows(new SecurityFailedException(), () -> client.ensureGoodResponse(stub));
    //    stub.setResponseFromString(200, "<html><body>Hello!</body></html>");
    //    assertThrows(new SecurityFailedException(), () -> client.ensureGoodResponse(stub));
    //    stub.setResponseFromString(403, "<html><body>Hello!</body></html>");
    //    assertThrows(new SecurityFailedException(), () -> client.ensureGoodResponse(stub));
    //};
}
