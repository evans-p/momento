package gr.evansp.momento.service;

import java.net.MalformedURLException;
import java.net.URI;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Service
public class SanitizeInputServiceImpl implements SanitizeInputService {

	@Override
	public String sanitizeStringInput(String input) {
		return Encode.forHtml(input);
	}

	@Override
	public String sanitizeUrl(String url) {
		String sanitizedUrl = null;
		if (url != null) {
			try {
				URI.create(url).toURL();
				sanitizedUrl = Encode.forUriComponent(url);
			} catch (MalformedURLException ignored) {
			}
		}

		return sanitizedUrl;
	}
}
