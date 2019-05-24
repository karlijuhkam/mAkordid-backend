package ee.heikokarli.makordid;

import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.exception.band.BandNotFoundException;
import ee.heikokarli.makordid.exception.song.SongNotFoundException;
import ee.heikokarli.makordid.service.BandService;
import ee.heikokarli.makordid.service.SongService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MakordidApplicationTests {

	//Bändide teenus
	@Autowired
	private BandService bandService;

	//Laulude teenus
	@Autowired
	private SongService songService;

	//Test leidmaks kõiki bände, hetkel on neid andmebaasis 2
	@Test
	public void testFindAllBands() {
		//Leiame teenusest kõik bändid
		List<Band> bandList = bandService.getAllBands();

		//Asserdime, et saadud vastus ei oleks "null"
		Assert.assertNotNull("Fail- expected not null", bandList);
		//Asserdime, et saadud bändide hulk oleks 2
		Assert.assertEquals("Fail- expected size", 2, bandList.size());
	}


	//Test leidmaks ühte laulu
	@Test
	public void testFindSong() {

		//Sätime paika ID
		Long id = new Long(1);

		//Leiame teenusest laulu ID abil
		Song song = songService.getSongById(id);

		//Asserdime, et vastus ei oleks "null"
		Assert.assertNotNull("Fail- expected not null", song);
		//Asserdime, et vaste id oleks sama, mis paika pandud id
		Assert.assertEquals("Fail- expected Id", id, song.getId());
		//Asserdime, et laulu nimi oleks õige
		Assert.assertEquals("Fail- expected name", "Tantsin sinuga taevas", song.getName());
	}

	//Testime, kas süsteem viskab õige veateate, kui laulu ei ole olemas- antud juhul 404 Song not found.
	@Test(expected = SongNotFoundException.class)
	public void testFindSongNotFound() {
		//Seame id'ks Longi maksimaalse väärtuse
		Long id = Long.MAX_VALUE;
		//Otsime lugu ning kuna selle IDga lugu puudub, visatakse exception
		Song song = songService.getSongById(id);
	}


}
