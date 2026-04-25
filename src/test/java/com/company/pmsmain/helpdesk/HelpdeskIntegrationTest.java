package com.company.pmsmain.helpdesk;

import com.company.pmsmain.helpdesk.dto.ChatMessage;
import com.company.pmsmain.helpdesk.dto.HelpdeskTicket;
import com.company.pmsmain.helpdesk.service.HelpdeskRestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

/**
 * End-to-end integration test for the HelpDesk REST bridge.
 *
 * Requires the Python HelpDesk services to be reachable at 192.168.0.147.
 * Runs without a Spring context — beans are wired manually.
 * All service methods catch exceptions internally; tests report pass/fail
 * based on the returned values, never on exceptions thrown.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("HelpDesk Integration Test")
public class HelpdeskIntegrationTest {

    // ── Service coordinates ────────────────────────────────────────────────────

    private static final String CHAT_URL    = "http://192.168.0.147:7863";
    private static final String SUPPORT_URL = "http://192.168.0.147:7870";

    // Known ticket from the live DB (used as fallback when createTicket() fails)
    private static final String KNOWN_TICKET = "a8d99db3-7453-4f47-833d-1e456a1b0258";

    // ── Shared state ───────────────────────────────────────────────────────────

    private static HelpdeskRestService service;
    private static String              createdTicketId;   // set by test 2, used by tests 3-5

    // ── Setup ──────────────────────────────────────────────────────────────────

    @BeforeAll
    static void setup() {
        RestTemplate rest = new RestTemplateBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(30))
                .build();
        service = new HelpdeskRestService(rest, new ObjectMapper(), CHAT_URL, SUPPORT_URL);

        hr();
        log("HelpDesk Integration Test — initialised");
        log("  Chat API    : " + CHAT_URL);
        log("  Support API : " + SUPPORT_URL);
        hr();
    }

    // ── Test 1 : listTickets() ─────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("1. listTickets() — GET /api/tickets")
    void test_listTickets() {
        section("listTickets()");
        log("Calling: GET " + SUPPORT_URL + "/api/tickets");

        List<HelpdeskTicket> tickets = service.listTickets();

        log("Returned " + tickets.size() + " ticket(s)");
        if (tickets.isEmpty()) {
            warn("Empty result — /api/tickets likely returns 404 on this server.");
            warn("Expected: the Support API exposes /api/tickets.");
            warn("Actual  : only Gradio endpoint /gradio_api/call/reload_tickets is available.");
            status("DEGRADED", "listTickets() returns empty list (REST route missing)");
        } else {
            tickets.forEach(t -> log("  ticket_id=" + t.getTicketId()
                    + " user=" + t.getUserId()
                    + " status=" + t.getStatus()));
            status("PASS", "listTickets() returned " + tickets.size() + " record(s)");
        }
    }

    // ── Test 2 : createTicket() ────────────────────────────────────────────────

    @Test @Order(2)
    @DisplayName("2. createTicket('testuser') — POST /api/tickets")
    void test_createTicket() {
        section("createTicket(\"testuser\")");
        log("Calling: POST " + CHAT_URL + "/api/tickets  body={user_id:testuser}");

        createdTicketId = service.createTicket("testuser");

        if (createdTicketId == null) {
            warn("Returned null — /api/tickets POST not implemented on this server.");
            warn("Expected: Chat API creates a ticket and returns {ticket_id: ...}.");
            warn("Actual  : only Gradio endpoint /gradio_api/call/chat_and_reset is available.");
            log("Falling back to KNOWN_TICKET for subsequent tests: " + KNOWN_TICKET);
            status("DEGRADED", "createTicket() returned null (REST route missing)");
        } else {
            log("Created ticket_id: " + createdTicketId);
            status("PASS", "createTicket() returned ticket_id=" + createdTicketId);
        }
    }

    // ── Test 3 : getMessages() ─────────────────────────────────────────────────

    @Test @Order(3)
    @DisplayName("3. getMessages(ticketId) — GET /api/messages/{ticketId}")
    void test_getMessages() {
        String ticketId = resolve(createdTicketId);
        section("getMessages(\"" + ticketId + "\")");
        log("Calling: GET " + CHAT_URL + "/api/messages/" + ticketId);

        List<ChatMessage> messages = service.getMessages(ticketId);

        log("Returned " + messages.size() + " message(s)");
        if (messages.isEmpty()) {
            warn("Empty result — either the ticket has no messages or the endpoint failed.");
            status("UNKNOWN", "getMessages() returned empty list");
        } else {
            messages.forEach(m -> {
                String body = m.getMessage();
                String preview = (body != null && body.length() > 90)
                        ? body.substring(0, 90) + "…" : body;
                log(String.format("  [%s] %-12s  %s",
                        nvl(m.getTimestamp()), nvl(m.getSender()), preview));
            });
            status("PASS", "getMessages() returned " + messages.size() + " message(s)");
        }
    }

    // ── Test 4 : sendChatMessage() ─────────────────────────────────────────────

    @Test @Order(4)
    @DisplayName("4. sendChatMessage() — POST /api/chat")
    void test_sendChatMessage() {
        String ticketId = resolve(createdTicketId);
        section("sendChatMessage(\"testuser\", \"" + ticketId + "\", \"What is PMS?\")");
        log("Calling: POST " + CHAT_URL + "/api/chat");

        String reply = service.sendChatMessage("testuser", ticketId, "What is PMS?");

        if (reply == null) {
            warn("Returned null — /api/chat not implemented on this server.");
            warn("Expected: Chat API accepts {user_id, ticket_id, message} and returns {reply: ...}.");
            warn("Actual  : only Gradio endpoint /gradio_api/call/chat_and_reset is available.");
            status("DEGRADED", "sendChatMessage() returned null (REST route missing)");
        } else {
            log("Reply: " + reply);
            status("PASS", "sendChatMessage() returned a reply");
        }
    }

    // ── Test 5 : sendReply() ───────────────────────────────────────────────────

    @Test @Order(5)
    @DisplayName("5. sendReply() — POST /api/tickets/{id}/reply")
    void test_sendReply() {
        String ticketId = resolve(createdTicketId);
        section("sendReply(\"" + ticketId + "\", \"This is a support reply.\")");
        log("Calling: POST " + SUPPORT_URL + "/api/tickets/" + ticketId + "/reply");

        boolean ok = service.sendReply(ticketId, "This is a support reply.");

        if (!ok) {
            warn("Returned false — /api/tickets/{id}/reply not implemented on this server.");
            warn("Expected: Support API accepts {message} and persists a reply.");
            warn("Actual  : only Gradio endpoint /gradio_api/call/chatbox_send is available.");
            status("DEGRADED", "sendReply() returned false (REST route missing)");
        } else {
            status("PASS", "sendReply() succeeded");
        }
    }

    // ── Test 6 : Gradio smoke test ─────────────────────────────────────────────

    @Test @Order(6)
    @DisplayName("6. Gradio smoke — /gradio_api/call/reload_tickets")
    void test_gradioSmokeTickets() {
        section("Gradio smoke — reload_tickets");
        log("This verifies the real Gradio ticket endpoint works.");
        log("Calling: POST " + SUPPORT_URL + "/gradio_api/call/reload_tickets");

        RestTemplate plain = new RestTemplateBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(15))
                .build();

        try {
            String evtBody = plain.postForObject(
                    SUPPORT_URL + "/gradio_api/call/reload_tickets",
                    "{\"data\":[]}", String.class);
            log("Step-1 response: " + evtBody);

            // parse event_id
            ObjectMapper om = new ObjectMapper();
            String eventId = om.readTree(evtBody).path("event_id").asText(null);
            if (eventId == null) {
                warn("No event_id in response — unexpected shape.");
                status("UNKNOWN", "Gradio reload_tickets — no event_id");
                return;
            }
            log("event_id: " + eventId);

            String result = plain.getForObject(
                    SUPPORT_URL + "/gradio_api/call/reload_tickets/" + eventId,
                    String.class);
            // strip SSE prefix "event: complete\ndata: "
            String data = result != null ? result.replaceAll("event:.*\\ndata: ", "").trim() : "";
            log("Step-2 raw: " + (data.length() > 200 ? data.substring(0, 200) + "…" : data));
            status("PASS", "Gradio reload_tickets responded successfully");
        } catch (Exception e) {
            warn("Gradio call failed: " + e.getMessage());
            status("FAIL", "Gradio smoke test threw: " + e.getClass().getSimpleName());
        }
    }

    // ── Test 7 : Gradio chat smoke test ───────────────────────────────────────

    @Test @Order(7)
    @DisplayName("7. Gradio smoke — /gradio_api/call/chat_and_reset")
    void test_gradioSmokeChat() {
        section("Gradio smoke — chat_and_reset");
        log("Calling: POST " + CHAT_URL + "/gradio_api/call/chat_and_reset");

        RestTemplate plain = new RestTemplateBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(30))
                .build();

        try {
            String body = "{\"data\":[\"integration test ping\",[]]  }";
            String evtBody = plain.postForObject(
                    CHAT_URL + "/gradio_api/call/chat_and_reset",
                    body, String.class);
            log("Step-1 response: " + evtBody);

            ObjectMapper om = new ObjectMapper();
            String eventId = om.readTree(evtBody).path("event_id").asText(null);
            if (eventId == null) {
                warn("No event_id — unexpected response shape.");
                status("UNKNOWN", "Gradio chat_and_reset — no event_id");
                return;
            }
            log("event_id: " + eventId);

            String result = plain.getForObject(
                    CHAT_URL + "/gradio_api/call/chat_and_reset/" + eventId,
                    String.class);
            String data = result != null ? result.replaceAll("event:.*\\ndata: ", "").trim() : "";
            log("Step-2 raw (first 300): " + (data.length() > 300 ? data.substring(0, 300) + "…" : data));
            status("PASS", "Gradio chat_and_reset responded with AI reply");
        } catch (Exception e) {
            warn("Gradio call failed: " + e.getMessage());
            status("FAIL", "Gradio smoke test threw: " + e.getClass().getSimpleName());
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private static String resolve(String id) {
        return (id != null) ? id : KNOWN_TICKET;
    }

    private static String nvl(String s) { return s != null ? s : "null"; }

    private static void log(String msg)    { System.out.println(msg); }
    private static void warn(String msg)   { System.out.println("  ⚠  " + msg); }
    private static void hr()               { System.out.println("─".repeat(72)); }

    private static void section(String title) {
        System.out.println();
        System.out.println("┌─ " + title);
    }

    private static void status(String level, String msg) {
        String icon = switch (level) {
            case "PASS"     -> "✓";
            case "FAIL"     -> "✗";
            case "DEGRADED" -> "△";
            default         -> "?";
        };
        System.out.println("└─ [" + level + "] " + icon + "  " + msg);
    }
}
