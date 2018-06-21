package com.katherineebel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Getter
@Component
public class GameImpl implements Game {

    // == fields ==
    @Getter(AccessLevel.NONE)
    private final NumberGenerator numberGenerator;
    private final int guessCount;
    private int number;
    private int smallest;
    private int biggest;
    @Setter
    private int guess;

    private int remainingGuesses;
    private boolean validNumberRange = true;

    // == constructor ==
    public GameImpl(@GuessCount int guessCount, NumberGenerator numberGenerator) {
        this.guessCount = guessCount;
        this.numberGenerator = numberGenerator;
    }

    // == init ==

    @PostConstruct
    @Override
    public void reset() {
        smallest = numberGenerator.getMinNumber();
        guess = numberGenerator.getMinNumber();
        remainingGuesses = guessCount;
        biggest = numberGenerator.getMaxNumber();
        number = numberGenerator.next();
        log.debug("the number is {}", number);
        log.debug("reset() called");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("in Game preDestroy()");
    }
    // == public methods ==

    @Override
    public void check() {
        checkValidNumberRange();
        if (validNumberRange) {
            if (guess > number) {
                biggest = guess - 1;
            }

            if (guess < number) {
                smallest = guess + 1;
            }
        }
        remainingGuesses--;
    }

    @Override
    public boolean isGameWon() {
        return guess == number;
    }

    @Override
    public boolean isGameLost() {
        return !isGameWon() && remainingGuesses <= 0;
    }

    // == private methods ==
    private void checkValidNumberRange() {
        validNumberRange = (guess >= smallest) && (guess <= biggest);
    }
}
