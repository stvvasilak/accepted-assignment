package accepted.vasilakakis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class ConverterConfig implements WebMvcConfigurer {

  private final MatchConverter matchConverter;
  private final MatchOddConverter matchOddConverter;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(matchConverter);
    registry.addConverter(matchOddConverter);
  }
}
