package ru.vzotov.cashreceipt.application;

import ru.vzotov.cashreceipt.domain.model.Check;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public interface CheckParsingService {
    Check parse(InputStream in) throws IOException;

    Check parse(String data) throws IOException;
}
