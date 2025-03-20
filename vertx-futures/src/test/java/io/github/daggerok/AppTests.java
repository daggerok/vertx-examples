package io.github.daggerok;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("App tests")
@ExtendWith(OutputCaptureExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AppTests {

  @Test
  void should_test_app(CapturedOutput output) {
    // when
    App.main();

    // then
    assertThat(output.getAll()).contains("Hello", "World!");
  }
}
