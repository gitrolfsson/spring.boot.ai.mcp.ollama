package se.rolfsson.mcp.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

@Slf4j
public class TestFunctions {

  private int age = 5;

  @Tool(name = "getDonaldTrumpFact", description = "Get the the alternative fact about Donald Trump")
  @SuppressWarnings("unused")
  public DonaldTrumpResponse getDonaldTrumpFact() {
    log.info("getDonaldTrumpFact called");
    return new DonaldTrumpResponse(new Age(this.age), "He is the best president ever");
  }

  @Tool(name = "setDonaldTrumpAge", description = "Set the age of Donald Trump")
  @SuppressWarnings("unused")
  //Do not use primitive types as parameters
  public String setDonaldTrumpAge(Integer age) {
    log.info("setDonaldTrumpAge called with age: {}", age);
    this.age = age;
    return "Donald trump has the age of " + age;
  }

  public record DonaldTrumpResponse(Age age, String alternativeFact) {
  }

  record Age(int age) {
  }
}
