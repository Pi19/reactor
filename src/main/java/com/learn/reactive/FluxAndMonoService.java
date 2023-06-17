package com.learn.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoService {

    /**
     *
     * @return
     */
    public Flux<String> nameFlux() {
        return Flux.fromIterable(List.of("bob" , "toto" , "pierrot"))
                .log();
    }

    /**
     *
     * @return
     */
    public Mono<String> nameMono(){
        return Mono.just("alex");
    }

    public Flux<String> nameFlux_map() {
        return Flux.fromIterable(List.of("bob" , "toto" , "pierrot"))
                .map(String::toUpperCase)
                .log();
    }


    public Flux<String> nameFlux_immutability() {
        var flux =  Flux.fromIterable(List.of("bob" , "toto" , "pierrot")) ;
        flux.map(String::toUpperCase);
        return  flux;

    }

    public Flux<String> nameFlux_mapWithFilter(int length) {
        return Flux.fromIterable(List.of("bob" , "toto" , "pierrot"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > length )
                .map(name -> name.length() + "-" +name)
                .log();
    }

    public Flux<String> nameFlux_flatMap(int length) {
        return Flux.fromIterable(List.of("bob" , "toto" , "pierrot"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > length )
                .flatMap(name -> splitString(name))
                .log();
    }


    public Flux<String> nameFlux_flatMap_async(int length) {
        return Flux.fromIterable(List.of("bob" , "toto" , "pierrot"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > length )
                .flatMap(name -> splitString_withDelay(name))
                .log();
    }

    /**
     * Garder l'ordre
     * @param length
     * @return
     */
    public Flux<String> nameFlux_concatMap_async(int length) {
        return Flux.fromIterable(List.of("bob" , "toto" , "pierrot"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > length )
                .concatMap(name -> splitString_withDelay(name))
                .log();
    }

    /**
     * TOTO => Flux(T,O,T,O)
     * @param name
     * @return
     */
    public Flux<String> splitString (String name ) {
        var charArray = name.split("");
        return  Flux.fromArray(charArray);
    }

    public Flux<String> splitString_withDelay (String name ) {
        var charArray = name.split("");
        var delay =  new Random().nextInt(1000);
        return  Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public Mono<String> namesMono_map_Filter(int stringLength) {
        return  Mono.just("titi")
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength);
    }

    public Mono<List<String>> namesMono_flatmap_Filter(int stringLength) {
        return  Mono.just("titi")
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();

    }

    public Flux<String> namesMono_flatMapMany_Filter(int stringLength) {
        return  Mono.just("titi")
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();

    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList= List.of(charArray);
        return  Mono.just(charList);
    }


    public Flux<String> nameFlux_transform(int length) {

        Function<Flux<String>,Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s-> s.length() > length);

        return Flux.fromIterable(List.of("bob" , "toto" , "pierrot"))
                .transform(filterMap)
                .flatMap(name -> splitString(name))
                .defaultIfEmpty("default")
                .log();
    }

    public Flux <String> explore_concat () {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        return  Flux.concat(abcFlux,defFlux);
    }


    public Flux <String> explore_concatWith () {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        return  abcFlux.concatWith(defFlux);
    }

    public Flux <String> explore_concatWithMono () {
        var aFlux = Mono.just("A");
        var dFlux = Mono.just("D");

        return  aFlux.concatWith(dFlux);//A,D
    }

    public Flux <String> explore_Merge () {
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100)).log();
        var defFlux = Flux.just("D","E","F").
                delayElements(Duration.ofMillis(125)).log();

        return  Flux.merge(abcFlux ,defFlux);
    }

    public Flux <String> explore_mergeSequentiel() {
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100)).log();
        var defFlux = Flux.just("D","E","F").
                delayElements(Duration.ofMillis(125)).log();

        return  Flux.mergeSequential(abcFlux ,defFlux);
    }


    public Flux <String> explore_Zip() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E").log();

        return  Flux.zip(abcFlux,defFlux,
                (first , second) -> first + second);
    }

    public Flux<String> nameFlux_transform_SwithIfEmpty(int length) {

        Function<Flux<String>,Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                        .filter(s-> s.length() > length)
                        .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("default")
                .map(String::toUpperCase)
                .flatMap(s -> splitString(s));//"D","E","F","A","U","L","T"

        return Flux.fromIterable(List.of("bob" , "toto" , "pierrot"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

}
