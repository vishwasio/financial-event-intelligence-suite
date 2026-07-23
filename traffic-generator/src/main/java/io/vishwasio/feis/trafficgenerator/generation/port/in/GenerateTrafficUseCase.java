package io.vishwasio.feis.trafficgenerator.generation.port.in;

/**
 * Inbound (driving) port for triggering traffic generation.
 *
 * <p>Traffic generation is entirely on-demand — there are no
 * schedulers or background jobs. A future REST adapter calls this
 * directly in response to POST /generate and POST /clear.
 */
public interface GenerateTrafficUseCase {

    GenerationResult generate(GenerationCommand command);

    void clear();
}
