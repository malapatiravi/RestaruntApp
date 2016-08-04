package com.example.malz.listview;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieData {

    List<Map<String,?>> moviesList;

    public List<Map<String, ?>> getMoviesList() {
        return moviesList;
    }

    public int getSize(){
        return moviesList.size();
    }
	public boolean setKey1(HashMap movie,String key,boolean state)
	{
		movie.put(key,state);
		return true;
	}
    public HashMap getItem(int i){
        if (i >=0 && i < moviesList.size()){
            return (HashMap) moviesList.get(i);
        } else return null;
    }
	public void addItem(int position, HashMap movie)
	{
		moviesList.add(position, movie);
	}
	public void removItem(int position, HashMap movie)
	{

		moviesList.remove(position);
	}
    public MovieData(){
        String description;
		String length;
		String year;
		double rating;
		String director;
		String stars;
		String url;
        moviesList = new ArrayList<Map<String,?>>();
        //#1-10
		year = "2009";
		length = "162 min";
		rating = 7.9;
		director = "Cameron" ;
		stars = "Sam Worthington, Zoe Saldana, Sigourney Weaver";
		url ="http://ia.media-imdb.com/images/M/MV5BMTYwOTEwNjAzMl5BMl5BanBnXkFtZTcwODc5MTUwMw@@._V1_SY317_CR0,0,214,317_AL_.jpg";
        description = "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.";
		moviesList.add(createMovie("Avatar", R.drawable.avatar, description, year, length, rating, director, stars, url));
		year = "1997";
		length = "194 min";
		rating = 7.7;
		director = "James Cameron" ;
		stars = "Leonardo DiCaprio, Kate Winslet, Billy Zane";
		url ="http://ia.media-imdb.com/images/M/MV5BMjExNzM0NDM0N15BMl5BanBnXkFtZTcwMzkxOTUwNw@@._V1_SY317_CR0,0,214,317_AL_.jpg";
        description = "Seventeen-year-old aristocrat, expecting to be married to a rich claimant by her mother, falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.";
		moviesList.add(createMovie("Titanic", R.drawable.titanic, description, year, length, rating, director, stars, url));
		year = "2012";
		length = "143 min";
		rating = 8.2;
		director = "Joss Whedon" ;
		stars = "Robert Downey Jr., Chris Evans, Scarlett Johansson";
		url ="http://ia.media-imdb.com/images/M/MV5BMTk2NTI1MTU4N15BMl5BanBnXkFtZTcwODg0OTY0Nw@@._V1_SY317_CR0,0,214,317_AL_.jpg";
		description = "Nick Fury of S.H.I.E.L.D. assembles a team of superheroes to save the Movie from Loki and his army.";
        moviesList.add(createMovie("The Avengers", R.drawable.avengers, description, year, length, rating, director, stars, url));
		year = "2008";
		length = "152 min";
		rating = 9.0;
		director = "Christopher Nolan" ;
		stars = "Christian Bale, Heath Ledger, Aaron Eckhart";
		url = "http://ia.media-imdb.com/images/M/MV5BMTk4ODQzNDY3Ml5BMl5BanBnXkFtZTcwODA0NTM4Nw@@._V1_SX214_AL_.jpg";
		description = "When Batman, Gordon and Harvey Dent launch an assault on the mob, they let the clown out of the box, the Joker, bent on turning Gotham on itself and bringing any heroes down to his level.";
        moviesList.add(createMovie("The Dark Knight", R.drawable.dark_knight_rises, description, year, length, rating, director, stars, url));
		year = "1999";
		length = "136 min";
		rating = 6.6;
		director = "George Lucas" ;
		stars = "Ewan McGregor, Liam Neeson, Natalie Portman";
		url ="http://ia.media-imdb.com/images/M/MV5BMTQ4NjEwNDA2Nl5BMl5BanBnXkFtZTcwNDUyNDQzNw@@._V1_SX214_AL_.jpg";
		description = "Two Jedi Knights escape a hostile blockade to find allies and come across a young boy who may bring balance to the Force, but the long dormant Sith resurface to reclaim their old glory.";
        moviesList.add(createMovie("Star Wars I", R.drawable.star_wars1, description, year, length, rating, director, stars, url));
		year = "1977";
		length = "121 min";
		rating = 8.7;
		director = "George Lucas" ;
		stars = "Mark Hamill, Harrison Ford, Carrie Fisher";
		url ="http://ia.media-imdb.com/images/M/MV5BMTU4NTczODkwM15BMl5BanBnXkFtZTcwMzEyMTIyMw@@._V1_SX214_AL_.jpg";
		description = "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a wookiee and two droids to save the universe from the Empire's world-destroying battle-station, while also attempting to rescue Princess Leia from the evil Darth Vader.";
        moviesList.add(createMovie("Star Wars IV ", R.drawable.star_wars4, description, year, length, rating, director, stars, url));
		year = "2012";
		length = "165 min";
		rating = 8.6;
		director = "Christopher Nolan" ;
		stars = "Christian Bale, Tom Hardy, Anne Hathaway";
		url ="http://ia.media-imdb.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SY317_CR0,0,214,317_AL_.jpg";
		description = "Eight years after the Joker's reign of anarchy, the Dark Knight must return to defend Gotham City against the enigmatic jewel thief Catwoman and the ruthless mercenary Bane as the city teeters on the brink of complete annihilation.";
        moviesList.add(createMovie("The Dark Knight Rises", R.drawable.dark_knight, description, year, length, rating, director, stars, url));
		year = "2004";
		length = "93 min";
		rating = 7.3;
		director = "Andrew Adamson, Kelly Asbury" ;
		stars = "Mike Myers, Eddie Murphy, Cameron Diaz";
		url ="http://ia.media-imdb.com/images/M/MV5BMTk4MTMwNjI4M15BMl5BanBnXkFtZTcwMjExMzUyMQ@@._V1_SX214_AL_.jpg";
		description = "Princess Fiona's parents invite her and Shrek to dinner to celebrate her marriage. If only they knew the newlyweds were both ogres.";
        moviesList.add(createMovie("Shrek 2", R.drawable.shrek2, description, year, length, rating, director, stars, url));
		year = "1982";
		length = "115 min";
		rating = 7.9;
		director = "Steven Spielberg" ;
		stars = "Henry Thomas, Drew Barrymore, Peter Coyote";
		url ="http://ia.media-imdb.com/images/M/MV5BMTc1NTQ0MTUyNF5BMl5BanBnXkFtZTcwMDYzMDU2Mw@@._V1_SX214_AL_.jpg";
		description = "A troubled child summons the courage to help a friendly alien escape Earth and return to his home-world.";
        moviesList.add(createMovie("E.T. the Extra-Terrestrial", R.drawable.et, description, year, length, rating, director, stars, url));
		year = "2013";
		length = "146 min";
		rating = 7.8;
		director = "Francis Lawrence" ;
		stars = "Jennifer Lawrence, Josh Hutcherson, Liam Hemsworth";
		url ="http://ia.media-imdb.com/images/M/MV5BMTAyMjQ3OTAxMzNeQTJeQWpwZ15BbWU4MDU0NzA1MzAx._V1_SX214_AL_.jpg";
		description = "Katniss Everdeen and Peeta Mellark become targets of the Capitol after their victory in the 74th Hunger Games sparks a rebellion in the Districts of Panem.";
		moviesList.add(createMovie("The Hunger Games: Catching Fire", R.drawable.hunger_games, description, year, length, rating, director, stars, url));
		
		//#11-20
		year = "2006";
		length = "151 min";
		rating = 7.3;
		director = "Gore Verbinski" ;
		stars = "Johnny Depp, Orlando Bloom, Keira Knightley";
		url ="http://ia.media-imdb.com/images/M/MV5BMTcwODc1MTMxM15BMl5BanBnXkFtZTYwMDg1NzY3._V1_SY317_CR0,0,214,317_AL_.jpg";
		description = "Jack Sparrow races to recover the heart of Davy Jones to avoid enslaving his soul to Jones' service, as other friends and foes seek the heart for their own agenda as well.";
        moviesList.add(createMovie("Pirates of the Caribbean: Dead Man's Chest", R.drawable.pirates, description, year, length, rating, director, stars, url));
		year = "1994";
		length = "89 min";
		rating = 8.5;
		director = "Roger Allers, Rob Minkoff" ;
		stars = "Matthew Broderick, Jeremy Irons, James Earl Jones";
		url ="http://ia.media-imdb.com/images/M/MV5BMjEyMzgwNTUzMl5BMl5BanBnXkFtZTcwNTMxMzM3Ng@@._V1_SY317_CR15,0,214,317_AL_.jpg";
		description = "Lion cub and future king Simba searches for his identity. His eagerness to please others and penchant for testing his boundaries sometimes gets him into trouble.";
        moviesList.add(createMovie("The Lion King", R.drawable.lion, description, year, length, rating, director, stars, url));
		year = "2010";
		length = "103 min";
		rating = 8.4;
		director = "Lee Unkrich" ;
		stars = "Tom Hanks, Tim Allen, Joan Cusack";
		url ="http://ia.media-imdb.com/images/M/MV5BMTgxOTY4Mjc0MF5BMl5BanBnXkFtZTcwNTA4MDQyMw@@._V1_SY317_CR5,0,214,317_AL_.jpg";
		description = "The toys are mistakenly delivered to a day-care center instead of the attic right before Andy leaves for college, and it's up to Woody to convince the other toys that they weren't abandoned and to return home.";
        moviesList.add(createMovie("Toy Story 3", R.drawable.toy3, description, year, length, rating, director, stars, url));
		year = "2013";
		length = "130 min";
		rating = 7.4;
		director = "Shane Black" ;
		stars = "Robert Downey Jr., Guy Pearce, Gwyneth Paltrow";
		url ="http://ia.media-imdb.com/images/M/MV5BMjIzMzAzMjQyM15BMl5BanBnXkFtZTcwNzM2NjcyOQ@@._V1_SX214_AL_.jpg";
		description = "When Tony Stark's world is torn apart by a formidable terrorist called the Mandarin, he starts an odyssey of rebuilding and retribution.";
        moviesList.add(createMovie("Iron Man 3", R.drawable.ironman3, description, year, length, rating, director, stars, url));
		year = "2012";
		length = "142 min";
		rating = 7.3;
		director = "Gary Ross" ;
		stars = "Jennifer Lawrence, Josh Hutcherson, Liam Hemsworth";
		url ="http://ia.media-imdb.com/images/M/MV5BMjA4NDg3NzYxMF5BMl5BanBnXkFtZTcwNTgyNzkyNw@@._V1_SX214_AL_.jpg";
		description = "Katniss Everdeen voluntarily takes her younger sister's place in the Hunger Games, a televised fight to the death in which two teenagers from each of the twelve Districts of Panem are chosen at random to compete.";
        moviesList.add(createMovie("The Hunger Games", R.drawable.hunger_games1, description, year, length, rating, director, stars, url));
		year = "2002";
		length = "121 min";
		rating = 7.3;
		director = "Sam Raimi" ;
		stars = "Tobey Maguire, Kirsten Dunst, Willem Dafoe";
		url ="http://ia.media-imdb.com/images/M/MV5BMzk3MTE5MDU5NV5BMl5BanBnXkFtZTYwMjY3NTY3._V1_SY317_CR0,0,214,317_AL_.jpg";
		description = "When bitten by a genetically modified spider, a nerdy, shy, and awkward high school student gains spider-like abilities that he eventually must use to fight evil as a superhero after tragedy befalls his family.";
        moviesList.add(createMovie("Spider-Man", R.drawable.spiderman, description, year, length, rating, director, stars, url));
		year = "1993";
		length = "127 min";
		rating = 8.0;
		director = "Steven Spielberg" ;
		stars = "Sam Neill, Laura Dern, Jeff Goldblum";
		url ="http://ia.media-imdb.com/images/M/MV5BMjQzODQyMzk2Nl5BMl5BanBnXkFtZTcwNTg4MjQ3OA@@._V1_SX214_AL_.jpg";
		description = "During a preview tour, a theme park suffers a major power breakdown that allows its cloned dinosaur exhibits to run amok.";
        moviesList.add(createMovie("Jurassic Park", R.drawable.jurassicpark, description, year, length, rating, director, stars, url));
		year = "2009";
		length = "150 min";
		rating = 6.0;
		director = "Michael Bay" ;
		stars = "Shia LaBeouf, Megan Fox, Josh Duhamel";
		url ="http://ia.media-imdb.com/images/M/MV5BNjk4OTczOTk0NF5BMl5BanBnXkFtZTcwNjQ0NzMzMw@@._V1_SX214_AL_.jpg";
		description = "Sam Witwicky leaves the Autobots behind for a normal life. But when his mind is filled with cryptic symbols, the Decepticons target him and he is dragged back into the Transformers' war.";
        moviesList.add(createMovie("Transformers: Revenge of the Fallen", R.drawable.transformers, description, year, length, rating, director, stars, url));
		year = "2013";
		length = "102 min";
		rating = 7.9;
		director = "Chris Buck, Jennifer Lee" ;
		stars = "Kristen Bell, Idina Menzel, Jonathan Groff";
		url ="http://ia.media-imdb.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX214_AL_.jpg";
		description = "Fearless optimist Anna teams up with Kristoff in an epic journey, encountering Everest-like conditions, and a hilarious snowman named Olaf in a race to find Anna's sister Elsa, whose icy powers have trapped the kingdom in eternal winter.";
        moviesList.add(createMovie("Frozen", R.drawable.frozen, description, year, length, rating, director, stars, url));
		year = "2011";
		length = "130 min";
		rating = 8.1;
		director = "David Yates" ;
		stars = "Daniel Radcliffe, Emma Watson, Rupert Grint";
		url ="http://ia.media-imdb.com/images/M/MV5BMTY2MTk3MDQ1N15BMl5BanBnXkFtZTcwMzI4NzA2NQ@@._V1_SX214_AL_.jpg";
		description = "Harry, Ron and Hermione search for Voldemort's remaining Horcruxes in their effort to destroy the Dark Lord as the final battle rages on at Hogwarts.";
        moviesList.add(createMovie("Harry Potter and the Deathly Hallows: Part 2", R.drawable.harry2, description, year, length, rating, director, stars, url));
		//#21-30
		year = "2003";
		length = "100 min";
		rating = 8.2;
		director = "Andrew Stanton, Lee Unkrich" ;
		stars = "Albert Brooks, Ellen DeGeneres, Alexander Gould ";
		url ="http://ia.media-imdb.com/images/M/MV5BMTY1MTg1NDAxOV5BMl5BanBnXkFtZTcwMjg1MDI5Nw@@._V1_SY317_CR0,0,214,317_AL_.jpg";
		description = "After his son is captured in the Great Barrier Reef and taken to Sydney, a timid clownfish sets out on a journey to bring him home.";
        moviesList.add(createMovie("Finding Nemo", R.drawable.nemo, description, year, length, rating, director, stars, url));
		year = "2005";
		length = "140 min";
		rating = 7.7;
		director = "George Lucas" ;
		stars = "Hayden Christensen, Natalie Portman, Ewan McGregor";
		url ="http://ia.media-imdb.com/images/M/MV5BNTc4MTc3NTQ5OF5BMl5BanBnXkFtZTcwOTg0NjI4NA@@._V1_SY317_CR12,0,214,317_AL_.jpg";
		description = "After three years of fighting in the Clone Wars, Anakin Skywalker falls prey to the Sith Lord's lies and makes an enemy of the Jedi and those he loves, concluding his journey to the Dark Side.";
        moviesList.add(createMovie("Star Wars III", R.drawable.star_wars3, description, year, length, rating, director, stars, url));
		year = "2003";
		length = "201 min";
		rating = 8.9;
		director = "Peter Jackson" ;
		stars = "Elijah Wood, Viggo Mortensen, Ian McKellen";
		url ="http://ia.media-imdb.com/images/M/MV5BMjE4MjA1NTAyMV5BMl5BanBnXkFtZTcwNzM1NDQyMQ@@._V1_SX214_AL_.jpg";
		description = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.";
        moviesList.add(createMovie("The Lord of the Rings: The Return of the King", R.drawable.rings, description, year, length, rating, director, stars, url));
		year = "2004";
		length = "127 min";
		rating = 7.4;
		director = "Sam Raimi" ;
		stars = "Tobey Maguire, Kirsten Dunst, Alfred Molina";
		url ="http://ia.media-imdb.com/images/M/MV5BMjE1ODcyODYxMl5BMl5BanBnXkFtZTcwNjA1NDE3MQ@@._V1_SY317_CR2,0,214,317_AL_.jpg";
		description = "Peter Parker is beset with troubles in his failing personal life as he battles a brilliant scientist named Doctor Otto Octavius.";
        moviesList.add(createMovie("Spider-Man 2", R.drawable.spiderman2, description, year, length, rating, director, stars, url));
		year = "2013";
		length = "98 min";
		rating = 7.6;
		director = "Pierre Coffin, Chris Renaud" ;
		stars = "Steve Carell, Kristen Wiig, Benjamin Bratt";
		url ="http://ia.media-imdb.com/images/M/MV5BMjExNjAyNTcyMF5BMl5BanBnXkFtZTgwODQzMjQ3MDE@._V1_SY317_CR0,0,214,317_AL_.jpg";
		description = "Gru is recruited by the Anti-Villain League to help deal with a powerful new super criminal.";
        moviesList.add(createMovie("Despicable Me 2", R.drawable.despicable2, description, year, length, rating, director, stars, url));
		year = "2011";
		length = "154 min";
		rating = 6.4;
		director = "Michael Bay" ;
		stars = "Shia LaBeouf, Rosie Huntington-Whiteley, Tyrese Gibson";
		url ="http://ia.media-imdb.com/images/M/MV5BMTkwOTY0MTc1NV5BMl5BanBnXkFtZTcwMDQwNjA2NQ@@._V1_SX214_AL_.jpg";
		description = "The Autobots learn of a Cybertronian spacecraft hidden on the moon, and race against the Decepticons to reach it and to learn its secrets.";
        moviesList.add(createMovie("Transformers: Dark of the Moon", R.drawable.transformers2, description, year, length, rating, director, stars, url));
		year = "2002";
		length = "179 min";
		rating = 8.8;
		director = "Peter Jackson" ;
		stars = "Elijah Wood, Ian McKellen, Viggo Mortensen";
		url ="http://ia.media-imdb.com/images/M/MV5BMTAyNDU0NjY4NTheQTJeQWpwZ15BbWU2MDk4MTY2Nw@@._V1_SY317_CR1,0,214,317_AL_.jpg";
		description = "While Frodo and Sam edge closer to Mordor with the help of the shifty Gollum, the divided fellowship makes a stand against Sauron's new ally, Saruman, and his hordes of Isengard.";
        moviesList.add(createMovie("The Lord of the Rings: The Two Towers", R.drawable.rings2, description, year, length, rating, director, stars, url));
		year = "2007";
		length = "139 min";
		rating = 6.3;
		director = "Sam Raimi" ;
		stars = "Tobey Maguire, Kirsten Dunst, Topher Grace";
		url ="http://ia.media-imdb.com/images/M/MV5BODUwMDc5Mzc5M15BMl5BanBnXkFtZTcwNDgzOTY0MQ@@._V1_SX214_AL_.jpg";
		description = "A strange black entity from another world bonds with Peter Parker and causes inner turmoil as he contends with new villains, temptations, and revenge.";
        moviesList.add(createMovie("Spider-Man 3", R.drawable.spiderman3, description, year, length, rating, director, stars, url));
		year = "2010";
		length = "108 min";
		rating = 6.5;
		director = "Tim Burton" ;
		url ="http://ia.media-imdb.com/images/M/MV5BMTMwNjAxMTc0Nl5BMl5BanBnXkFtZTcwODc3ODk5Mg@@._V1_SY317_CR0,0,214,317_AL_.jpg";
		stars = "Mia Wasikowska, Johnny Depp, Helena Bonham Carter";
		description = "Nineteen-year-old Alice returns to the magical world from her childhood adventure, where she reunites with her old friends and learns of her true destiny: to end the Red Queen's reign of terror.";
        moviesList.add(createMovie("Alice in Wonderland", R.drawable.alice, description, year, length, rating, director, stars, url));
		year = "1994";
		length = "145 min";
		rating = 8.8;
		director = "Robert Zemeckis" ;
		stars = "Tom Hanks, Robin Wright, Gary Sinise";
		url ="http://ia.media-imdb.com/images/M/MV5BMTQwMTA5MzI1MF5BMl5BanBnXkFtZTcwMzY5Mzg3OA@@._V1_SX214_AL_.jpg";
		description = "Forrest Gump, while not intelligent, has accidentally been present at many historic moments, but his true love, Jenny Curran, eludes him.";
        moviesList.add(createMovie("Forrest Gump", R.drawable.forrest_gump, description, year, length, rating, director, stars, url));		
    }
	public int findFirst(String query) {
		int i, movieSize = moviesList.size();
		for (i=0; i< movieSize; i++)
		{
			if (moviesList.get(i).get("name").toString().toLowerCase().contains(query.toLowerCase())) {

				return i;
			}
			else
			{

			}

		}
		return -1;
	}


    private HashMap createMovie(String name, int image, String description, String year,
                                String length, double rating, String director, String stars, String url) {
        HashMap movie = new HashMap();
        movie.put("image",image);
        movie.put("name", name);
        movie.put("description", description);
		movie.put("year", year);
		movie.put("length",length);
		movie.put("rating",rating);
		movie.put("director",director);
		movie.put("stars",stars);
		movie.put("url",url);
		movie.put("selection",false);
        return movie;
    }
}
