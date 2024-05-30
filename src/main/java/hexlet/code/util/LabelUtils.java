package hexlet.code.util;

import hexlet.code.model.Label;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class LabelUtils {

    @Bean
    public List<Label> getDefaultLabels() {

        var featureLabel = createLabel("feature");
        var bugLabel = createLabel("bug");

        return List.of(
                featureLabel,
                bugLabel
        );
    }

    private Label createLabel(String name) {

        var label = new Label();

        label.setName(name);

        return label;
    }
}
