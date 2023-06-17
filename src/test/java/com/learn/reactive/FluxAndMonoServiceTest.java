package com.learn.reactive;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoServiceTest {

    FluxAndMonoService fluxAndMonoGeneratorService = new FluxAndMonoService();


    @Test
    void namesFlux() {
        //given

        //when
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux();

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("bob","toto","pierrot")
                //.expectNextCount(3)
                .expectNext("bob")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void nameFlux_map() {
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_map();
        StepVerifier.create(namesFlux)
                .expectNext("BOB","TOTO","PIERROT")
                .verifyComplete();
    }

    @Test
    void nameFlux_immutability() {
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_immutability();
        StepVerifier.create(namesFlux)
                .expectNext("bob","toto","pierrot")
                .verifyComplete();
    }

    @Test
    void nameFlux_mapWithFilter() {
        //given
        int stringLength=3;

        //when
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_mapWithFilter(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("4-TOTO","7-PIERROT")
                .verifyComplete();
    }

    @Test
    void nameFlux_flatMap() {
        //given
        int stringLength=3;

        //when
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_flatMap(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("T","O","T","O","P","I","E","R","R","O","T")
                .verifyComplete();

    }

    @Test
    void nameFlux_flatMap_async() {
        //given
        int stringLength=3;

        //when
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_flatMap_async(stringLength);
        StepVerifier.create(namesFlux)
                //.expectNext("T","O","T","O","P","I","E","R","R","O","T")
                .expectNextCount(11)
                .verifyComplete();
    }

    @Test
    void nameFlux_concatMap_async() {
        //given
        int stringLength=3;

        //when
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_concatMap_async(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("T","O","T","O","P","I","E","R","R","O","T")
                .verifyComplete();
    }

    @Test
    void namesMono_flatmap_Filter() {
        int value = 3;
        var flux =  fluxAndMonoGeneratorService.namesMono_flatmap_Filter(value);
        StepVerifier.create(flux)
                .expectNext(List.of("T","I","T","I"))
                .verifyComplete();

    }

    @Test
    void namesMono_flatMapMany_Filter() {
        int value = 3;
        var flux =  fluxAndMonoGeneratorService.namesMono_flatMapMany_Filter(value);
        StepVerifier.create(flux)
                .expectNext("T","I","T","I")
                .verifyComplete();
    }

    @Test
    void nameFlux_transform() {
        //given
        int stringLength=3;

        //when
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_transform(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("T","O","T","O","P","I","E","R","R","O","T")
                .verifyComplete();
    }

    @Test
    void nameFlux_transformDefaultValue() {
        //given
        int stringLength=10;

        //when
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_transform(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void nameFlux_transform_SwithIfEmpty() {
        //given
        int stringLength=10;

        //when
        var namesFlux =  fluxAndMonoGeneratorService.nameFlux_transform_SwithIfEmpty(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        var concatFlux =  fluxAndMonoGeneratorService.explore_concat();
        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();

    }

    @Test
    void explore_Merge() {
        var concatFlux =  fluxAndMonoGeneratorService.explore_Merge();
        StepVerifier.create(concatFlux)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequentiel() {
        var concatFlux =  fluxAndMonoGeneratorService.explore_mergeSequentiel();
        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_Zip() {
        var zipFlux =  fluxAndMonoGeneratorService.explore_Zip();
        StepVerifier.create(zipFlux)
                .expectNext("AD","BE")
                .verifyComplete();
    }
}
