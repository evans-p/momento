package gr.evansp.momento;

import java.util.Optional;
import java.util.UUID;

import gr.evansp.momento.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Asset}.
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {

	/**
	 * Find asset by contentHash.
	 *
	 * @param contentHash contentHash
	 *
	 * @return Asset
	 */
	Optional<Asset> findByContentHash(String contentHash);

	/**
	 * Find asset by path.
	 *
	 * @param path path
	 *
	 * @return Asset
	 */
	Optional<Asset> findByPath(String path);
}
