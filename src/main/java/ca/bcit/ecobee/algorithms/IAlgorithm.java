package ca.bcit.ecobee.algorithms;

import java.util.List;

import ca.bcit.ecobee.models.EcobeeClient;
import ca.bcit.ecobee.models.Result;

public interface IAlgorithm {
	
	List<Result> simulate(List<EcobeeClient> clients);
	
}
