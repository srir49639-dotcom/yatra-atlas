package data;

import model.*;

/**
 * Authoritative Indian Travel Dataset with 20 iconic destinations.
 * Maintains full semantic consistency across destinations, attractions, hotels,
 * restaurants, local transportation, and intercity travel edges.
 */
public class TravelData {

    public static final Destination[] DESTINATIONS = new Destination[] {
        // 1. Hyderabad
        new Destination(
            "hyderabad", "Hyderabad", "Telangana", "Heritage",
            "The City of Pearls, Hyderabad effortlessly blends a 400-year-old Nizami royal heritage with a bustling modern tech metropolis. Famous for historic minarets, ancient hill forts, delectable Hyderabadi dum biryani, and artisanal pearl markets.",
            "/images/dest_hyderabad.jpg",
            17.3850, 78.4867, 3200, "October to March",
            new Attraction[] {
                new Attraction("hyd-1", "Charminar", "hyderabad", "Heritage", "16th-century grand mosque with four iconic 56m-tall granite minarets standing at the historic heart of the walled city.", 25, 4.7, "09:30 AM - 05:30 PM", "/images/attr_hyd-1.jpg"),
                new Attraction("hyd-2", "Golconda Fort", "hyderabad", "Heritage", "Spectacular medieval fortress known for ingenious acoustic signaling and the vault that once held the Koh-i-Noor diamond.", 25, 4.6, "09:00 AM - 05:30 PM", "/images/attr_hyd-2.jpg"),
                new Attraction("hyd-3", "Taj Falaknuma Palace", "hyderabad", "Luxury", "Exquisite Italian marble palace perched 2,000 feet above Hyderabad, former residence of the world's richest Nizam.", 500, 4.9, "Guided Tours on Weekends", "/images/attr_hyd-3.jpg"),
                new Attraction("hyd-4", "Salar Jung Museum", "hyderabad", "Museum", "One of the three National Museums of India housing the legendary Veiled Rebecca marble statue and rare antiquities.", 50, 4.5, "10:00 AM - 05:00 PM", "/images/attr_hyd-4.jpg")
            },
            new Hotel[] {
                new Hotel("h-hyd-1", "Taj Falaknuma Palace Hotel", "hyderabad", "Luxury", 35000, 4.9, "Engine Bowli, Falaknuma", new String[]{"Royal Carriage Arrival", "Spa", "Heritage Walks", "Fine Dining"}),
                new Hotel("h-hyd-2", "ITC Kakatiya Luxury Collection", "hyderabad", "Luxury", 9500, 4.7, "Begumpet, Hyderabad", new String[]{"Pool", "Awadhi Cuisine", "Gym", "Business Lounge"}),
                new Hotel("h-hyd-3", "The Golkonda Hotel", "hyderabad", "Mid-Range", 4200, 4.3, "Masab Tank, Banjara Hills", new String[]{"WiFi", "Restaurant", "City View"})
            },
            new Restaurant[] {
                new Restaurant("r-hyd-1", "Bawarchi Restaurant", "hyderabad", "Hyderabadi", 850, 4.6, "Special Mutton Dum Biryani", "RTC X Roads, Chikkadpally"),
                new Restaurant("r-hyd-2", "Paradise Biryani", "hyderabad", "Hyderabadi", 900, 4.4, "Royal Chicken Biryani & Double Ka Meetha", "SD Road, Secunderabad"),
                new Restaurant("r-hyd-3", "Chutneys", "hyderabad", "South Indian", 600, 4.5, "Guntur Idli & 7-Variety Chutney Platter", "Banjara Hills Road No. 3")
            },
            new Transport[] {
                new Transport("t-hyd-1", "hyderabad", "Flight", "Rajiv Gandhi International Airport (RGIA)", "Continuous Domestic & Global Flights", 4500, "Shamshabad, 25km south"),
                new Transport("t-hyd-2", "hyderabad", "Train", "Secunderabad & Hyderabad Deccan (Nampally)", "Daily express to all metros", 1200, "Major Indian Railways junctions"),
                new Transport("t-hyd-3", "hyderabad", "Metro", "Hyderabad Metro Rail (Red, Blue, Green Lines)", "Every 4-7 minutes", 45, "Connects Miyapur, Hitec City, and LB Nagar")
            }
        ),

        // 2. Goa
        new Destination(
            "goa", "Goa", "Goa", "Beach",
            "India's coastal paradise blessed with sun-drenched Arabian Sea beaches, historic Portuguese colonial baroque architecture, serene coastal backwaters, vibrant spice plantations, and laid-back seaside shacks.",
            "/images/dest_goa.jpg",
            15.2993, 74.1240, 4500, "November to February",
            new Attraction[] {
                new Attraction("goa-1", "Basilica of Bom Jesus", "goa", "Heritage", "UNESCO World Heritage 16th-century baroque basilica holding the sacred relics of St. Francis Xavier.", 0, 4.7, "09:00 AM - 06:30 PM", "/images/attr_goa-1.jpg"),
                new Attraction("goa-2", "Fort Aguada & Lighthouse", "goa", "Heritage", "Portuguese seventeenth-century lighthouse and fortress overlooking the vast expanse of the Arabian Sea.", 50, 4.5, "09:30 AM - 06:00 PM", "/images/attr_goa-2.jpg"),
                new Attraction("goa-3", "Calangute & Baga Beach", "goa", "Beach", "The buzzing queen of North Goa beaches featuring watersports, lively beach shacks, and golden coastal sunsets.", 0, 4.4, "Open 24 Hours", "/images/attr_goa-3.jpg"),
                new Attraction("goa-4", "Dudhsagar Waterfalls", "goa", "Adventure", "Four-tiered milky-white cascade tumbling 310 meters down steep Western Ghat cliffs.", 100, 4.8, "06:00 AM - 05:00 PM", "/images/attr_goa-4.jpg")
            },
            new Hotel[] {
                new Hotel("h-goa-1", "Taj Exotica Resort & Spa", "goa", "Luxury", 28000, 4.8, "Benaulim, South Goa", new String[]{"Private Beach", "Golf", "Ayurvedic Spa", "Pool"}),
                new Hotel("h-goa-2", "W Goa", "goa", "Luxury", 22000, 4.7, "Vagator Beach", new String[]{"Sunset Deck", "Spa", "Infinity Pool"}),
                new Hotel("h-goa-3", "Santana Beach Resort", "goa", "Mid-Range", 4800, 4.4, "Candolim", new String[]{"Beach Access", "Pool", "Gardens"})
            },
            new Restaurant[] {
                new Restaurant("r-goa-1", "Fisherman's Wharf", "goa", "Seafood & Goan", 1600, 4.7, "Goan Prawn Curry & Kingfish Rava Fry", "Cavelossim Riverfront"),
                new Restaurant("r-goa-2", "Gunpowder", "goa", "Coastal Indian", 1400, 4.6, "Kerala Beef Roast & Appam", "Assagao, North Goa"),
                new Restaurant("r-goa-3", "Thalassa Greek Taverna", "goa", "Mediterranean", 2200, 4.8, "Grilled Calamari & Baklava", "Siolim Waterfront")
            },
            new Transport[] {
                new Transport("t-goa-1", "goa", "Flight", "Manohar International (MOPA) & Dabolim Airport", "Daily domestic and international charters", 4200, "North and South Goa terminals"),
                new Transport("t-goa-2", "goa", "Train", "Madgaon & Thivim Railway Stations", "Konkan Railway connects Mumbai and South India", 950, "Scenic Konkan rail corridor"),
                new Transport("t-goa-3", "goa", "Taxi", "Goa Miles App & Self-Drive Rentals", "On-demand throughout state", 600, "Scooters, open jeeps, and cabs")
            }
        ),

        // 3. Delhi
        new Destination(
            "delhi", "Delhi", "Delhi", "Metropolitan",
            "India's vibrant historic capital standing as a living palimpsest of empires. From Mughal red sandstone forts and bustling Chandni Chowk bazaars to stately British Lutyens boulevards and cutting-edge diplomatic enclaves.",
            "/images/dest_delhi.jpg",
            28.6139, 77.2090, 4000, "October to March",
            new Attraction[] {
                new Attraction("del-1", "India Gate", "delhi", "Heritage", "Soaring 42-meter triumphal arch war memorial anchoring Rajpath and Kartavya Path.", 0, 4.7, "Open 24 Hours", "/images/attr_del-1.jpg"),
                new Attraction("del-2", "Red Fort (Lal Qila)", "delhi", "Heritage", "Massive red sandstone fortified palace built by Mughal Emperor Shah Jahan in 1639.", 35, 4.6, "09:30 AM - 04:30 PM (Mon Closed)", "/images/attr_del-2.jpg"),
                new Attraction("del-3", "Qutub Minar", "delhi", "Heritage", "UNESCO World Heritage 73-meter fluted minaret founded in 1192 by Qutb-ud-din Aibak.", 40, 4.6, "07:00 AM - 07:00 PM", "/images/attr_del-3.jpg"),
                new Attraction("del-4", "Humayun's Tomb", "delhi", "Heritage", "Sublime Mughal garden tomb and architectural precursor to the Taj Mahal.", 40, 4.8, "06:00 AM - 06:00 PM", "/images/attr_del-4.jpg")
            },
            new Hotel[] {
                new Hotel("h-del-1", "The Imperial New Delhi", "delhi", "Luxury", 24000, 4.8, "Janpath, Connaught Place", new String[]{"Heritage Art Gallery", "Spa", "Fine Dining"}),
                new Hotel("h-del-2", "The Leela Palace New Delhi", "delhi", "Luxury", 26000, 4.9, "Chanakyapuri Diplomatic Enclave", new String[]{"Rooftop Pool", "Michelin-tier dining"}),
                new Hotel("h-del-3", "Bloomrooms @ Janpath", "delhi", "Mid-Range", 4500, 4.3, "Connaught Place", new String[]{"WiFi", "Cafe", "Metro Access"})
            },
            new Restaurant[] {
                new Restaurant("r-del-1", "Karim's Historic Old Delhi", "delhi", "Mughlai", 1100, 4.6, "Mutton Korma & Sheermal", "Gali Kababian, Jama Masjid"),
                new Restaurant("r-del-2", "Bukhara @ ITC Maurya", "delhi", "North Indian", 4500, 4.9, "Dal Bukhara & Sikandari Raan", "Diplomatic Enclave"),
                new Restaurant("r-del-3", "Saravana Bhavan", "delhi", "South Indian", 550, 4.5, "Ghee Roast Dosa & Filter Coffee", "Janpath, Connaught Place")
            },
            new Transport[] {
                new Transport("t-del-1", "delhi", "Flight", "Indira Gandhi International (IGI T3)", "Primary Indian global aviation hub", 4800, "Domestic & Global terminals"),
                new Transport("t-del-2", "delhi", "Metro", "Delhi Metro Rail Corporation (DMRC)", "World-class 12-line subway network", 40, "Express line to Airport in 19 mins"),
                new Transport("t-del-3", "delhi", "Train", "New Delhi & Old Delhi Railway Stations", "Origin of Vande Bharat & Rajdhani expresses", 1100, "Heart of national rail system")
            }
        ),

        // 4. Varanasi
        new Destination(
            "varanasi", "Varanasi", "Uttar Pradesh", "Spiritual",
            "One of the oldest continuously inhabited cities on earth. Sacred Banaras is the spiritual heart of Hinduism, where ancient ghats step down to the holy Ganga, mesmerizing evening Ganga Aarti bells resound, and labyrinthine silk alleys enchant.",
            "/images/dest_varanasi.jpg",
            25.3176, 82.9739, 2500, "November to February",
            new Attraction[] {
                new Attraction("var-1", "Dashashwamedh Ghat", "varanasi", "Spiritual", "The most vibrant and celebrated ghat where priests perform the captivating choreographed evening Ganga Aarti.", 0, 4.9, "Ganga Aarti at 06:30 PM", "/images/attr_var-1.jpg"),
                new Attraction("var-2", "Kashi Vishwanath Temple", "varanasi", "Spiritual", "The revered golden-spired temple dedicated to Lord Shiva, housing one of the twelve sacred Jyotirlingas.", 0, 4.8, "03:00 AM - 11:00 PM", "/images/attr_var-2.jpg"),
                new Attraction("var-3", "Sarnath Deer Park & Stupa", "varanasi", "Heritage", "The hallowed site where Gautama Buddha taught his first sermon after attaining enlightenment.", 25, 4.7, "09:00 AM - 05:00 PM", "/images/attr_var-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-var-1", "BrijRama Palace Heritage Hotel", "varanasi", "Luxury", 26000, 4.9, "Darbhanga Ghat", new String[]{"Ghat Boat Arrival", "Classical Music", "Pure Vegetarian"}),
                new Hotel("h-var-2", "Taj Ganges Varanasi", "varanasi", "Luxury", 13000, 4.7, "Nadesar Palace Compound", new String[]{"Lush Gardens", "Pool", "Spa"}),
                new Hotel("h-var-3", "Scindhia Guest House", "varanasi", "Budget", 2200, 4.2, "Scindhia Ghat", new String[]{"River Balcony", "Rooftop Cafe"})
            },
            new Restaurant[] {
                new Restaurant("r-var-1", "Kashi Chaat Bhandar", "varanasi", "Street Food", 250, 4.7, "Tamatar Chaat & Dahi Golgappe", "Godowlia Chowk"),
                new Restaurant("r-var-2", "Blue Lassi Shop", "varanasi", "Dessert", 150, 4.6, "Rabdi Malai & Mango Pista Lassi", "Bangali Tola near Manikarnika"),
                new Restaurant("r-var-3", "Dosa Cafe", "varanasi", "Cafe", 400, 4.5, "Cheese Masala Dosa & Banarasi Chai", "Maan Mandir Ghat")
            },
            new Transport[] {
                new Transport("t-var-1", "varanasi", "Flight", "Lal Bahadur Shastri Airport (VNS)", "Direct flights from Delhi, Mumbai, Bengaluru", 4100, "Babatpur, 26km northwest"),
                new Transport("t-var-2", "varanasi", "Train", "Varanasi Junction (BSB) & Pt. Deen Dayal Upadhyaya", "Vande Bharat connecting to New Delhi in 8 hrs", 900, "Key junction on Northern Railway"),
                new Transport("t-var-3", "varanasi", "Boat", "Traditional Wooden Hand-Rowed Riverboats", "Sunrise and sunset cruises across all 84 ghats", 300, "Board at Dashashwamedh or Assi Ghat")
            }
        ),

        // 5. Bengaluru
        new Destination(
            "bengaluru", "Bengaluru", "Karnataka", "Metropolitan",
            "The Silicon Valley of India and the historic Garden City. Known for its breezy year-round pleasant climate, sprawling botanical parks, thriving craft microbrewery culture, and premier science and research institutions.",
            "/images/dest_bengaluru.jpg",
            12.9716, 77.5946, 3800, "September to March",
            new Attraction[] {
                new Attraction("blr-1", "Lalbagh Botanical Garden", "bengaluru", "Nature", "240-acre historic garden commissioned by Hyder Ali featuring a Victorian glass house.", 25, 4.6, "06:00 AM - 07:00 PM", "/images/attr_blr-1.jpg"),
                new Attraction("blr-2", "Bangalore Palace", "bengaluru", "Heritage", "Tudor-style royal estate modeled after Windsor Castle, decorated with wood carvings and oil paintings.", 230, 4.4, "10:00 AM - 05:30 PM", "/images/attr_blr-2.jpg"),
                new Attraction("blr-3", "Cubbon Park & Vidhana Soudha", "bengaluru", "Heritage", "Neo-Dravidian state legislative monument bordering lush 300-acre bamboo and teak groves.", 0, 4.7, "Open daylight hours", "/images/attr_blr-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-blr-1", "The Leela Palace Bengaluru", "bengaluru", "Luxury", 21000, 4.9, "Old Airport Road, Kodihalli", new String[]{"Art-filled Palatial Grounds", "Spa", "Rooftop Pool"}),
                new Hotel("h-blr-2", "Taj West End Bengaluru", "bengaluru", "Luxury", 16000, 4.8, "Race Course Road", new String[]{"20-Acre Heritage Park", "Colonial Charm"}),
                new Hotel("h-blr-3", "Ibis Bengaluru City Centre", "bengaluru", "Mid-Range", 4200, 4.2, "Richmond Road", new String[]{"Modern Rooms", "Bar", "Central Location"})
            },
            new Restaurant[] {
                new Restaurant("r-blr-1", "MTR (Mavalli Tiffin Room)", "bengaluru", "South Indian", 450, 4.7, "Rava Idli & Pure Ghee Masala Dosa", "Lalbagh Road"),
                new Restaurant("r-blr-2", "Vidyarthi Bhavan", "bengaluru", "South Indian", 300, 4.8, "Crisp Golden Benne Dosa", "Gandhi Bazaar, Basavanagudi"),
                new Restaurant("r-blr-3", "Toit Brewpub", "bengaluru", "Continental & Craft", 1800, 4.7, "Tint-in-Wit Beer & Wood-fired Pizzas", "100ft Road, Indiranagar")
            },
            new Transport[] {
                new Transport("t-blr-1", "bengaluru", "Flight", "Kempegowda International Airport (BLR T2)", "Premier bamboo garden terminal", 4200, "Devanahalli, 35km north"),
                new Transport("t-blr-2", "bengaluru", "Metro", "Namma Metro (Purple & Green Lines)", "Spans Whitefield to Kengeri", 45, "Air-conditioned fast transit"),
                new Transport("t-blr-3", "bengaluru", "Train", "KSR Bengaluru City & Yesvantpur Junction", "South Western Railway hub", 850, "Connects Chennai, Mysore, Mumbai")
            }
        ),

        // 6. Chennai
        new Destination(
            "chennai", "Chennai", "Tamil Nadu", "Cultural",
            "The cultural capital of South India, Chennai is celebrated for ancient Chola temple architecture, classical Carnatic music and Bharatanatyam dance, scenic Marina coastal promenade, and world-class healthcare.",
            "/images/dest_chennai.jpg",
            13.0827, 80.2707, 3000, "November to February",
            new Attraction[] {
                new Attraction("chn-1", "Kapaleeshwarar Temple", "chennai", "Heritage", "7th-century Dravidian masterpiece adorned with towering rainbow-hued gopuram dedicated to Lord Shiva.", 0, 4.8, "06:00 AM - 12:30 PM, 04:00 PM - 09:30 PM", "/images/attr_chn-1.jpg"),
                new Attraction("chn-2", "Marina Beach Promenade", "chennai", "Beach", "World's second-longest natural urban beach stretching 13 kilometers along the Bay of Bengal.", 0, 4.5, "Open 24 Hours", "/images/attr_chn-2.jpg"),
                new Attraction("chn-3", "Fort St. George & Museum", "chennai", "Heritage", "First English fortress erected in India (1644), now housing the Tamil Nadu Assembly and historic weapons museum.", 20, 4.4, "09:00 AM - 05:00 PM", "/images/attr_chn-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-chn-1", "Taj Coromandel", "chennai", "Luxury", 14000, 4.8, "Nungambakkam", new String[]{"Fine Southern Dining", "Spa", "Pool"}),
                new Hotel("h-chn-2", "The Leela Palace Chennai", "chennai", "Luxury", 18000, 4.9, "Adyar Seaface", new String[]{"Ocean View Rooms", "Infinity Pool"}),
                new Hotel("h-chn-3", "Residency Towers", "chennai", "Mid-Range", 5200, 4.4, "T. Nagar Shopping District", new String[]{"Central Location", "Buffet"})
            },
            new Restaurant[] {
                new Restaurant("r-chn-1", "Murugan Idli Shop", "chennai", "South Indian", 350, 4.7, "Melt-in-mouth Podi Idli & Jigarthanda", "T. Nagar & Besant Nagar"),
                new Restaurant("r-chn-2", "Dakshin @ Crowne Plaza", "chennai", "South Indian Fine Dining", 3200, 4.8, "Vasantha Neer & Meen Varuval", "Alwarpet"),
                new Restaurant("r-chn-3", "Annalakshmi Restaurant", "chennai", "Vegetarian", 1200, 4.6, "Traditional South Indian Thali Banquet", "Egmore")
            },
            new Transport[] {
                new Transport("t-chn-1", "chennai", "Flight", "Chennai International Airport (MAA)", "Direct flights to Asia, Europe, and domestic hubs", 3900, "Meenambakkam, 18km southwest"),
                new Transport("t-chn-2", "chennai", "Train", "Chennai Central (MGR) & Egmore", "Southern Railway Headquarters", 800, "Hub for all trains to South India"),
                new Transport("t-chn-3", "chennai", "Metro", "Chennai Metro Rail (CMRL)", "Direct link from Central station to Airport", 40, "Fast, air-conditioned elevated & underground rail")
            }
        ),

        // 7. Mumbai
        new Destination(
            "mumbai", "Mumbai", "Maharashtra", "Metropolitan",
            "The City of Dreams and India's economic capital. Dynamic and unstoppable, Mumbai features dramatic Victorian Gothic architecture, the iconic Gateway of India, glittering Marine Drive Queens Necklace, and Bollywood glamour.",
            "/images/dest_mumbai.jpg",
            19.0760, 72.8777, 5000, "November to February",
            new Attraction[] {
                new Attraction("mum-1", "Gateway of India", "mumbai", "Heritage", "26-meter basalt triumphal arch facing Mumbai harbour, built to commemorate King George V in 1911.", 0, 4.7, "Open 24 Hours", "/images/attr_mum-1.jpg"),
                new Attraction("mum-2", "Marine Drive (Queen's Necklace)", "mumbai", "Scenic", "3.6-kilometer arc-shaped coastal boulevard offering glorious Arabian Sea sunsets and dazzling night illuminations.", 0, 4.8, "Open 24 Hours", "/images/attr_mum-2.jpg"),
                new Attraction("mum-3", "Elephanta Caves", "mumbai", "Heritage", "UNESCO World Heritage rock-cut temples on Gharapuri Island with the awe-inspiring 7-meter Trimurti Shiva sculpture.", 40, 4.6, "09:00 AM - 05:30 PM (Ferry from Gateway)", "/images/attr_mum-3.jpg"),
                new Attraction("mum-4", "Chhatrapati Shivaji Maharaj Terminus", "mumbai", "Heritage", "UNESCO Victorian Gothic revival architectural masterpiece and busy railway headquarters.", 0, 4.7, "Open 24 Hours", "/images/attr_mum-4.jpg")
            },
            new Hotel[] {
                new Hotel("h-mum-1", "The Taj Mahal Palace & Tower", "mumbai", "Luxury", 34000, 4.9, "Apollo Bunder, Colaba", new String[]{"Iconic Sea Facing Heritage", "Sea Lounge High Tea", "Spa"}),
                new Hotel("h-mum-2", "The Oberoi Mumbai", "mumbai", "Luxury", 28000, 4.9, "Marine Drive, Nariman Point", new String[]{"Ocean View", "Michelin Experience", "Pool"}),
                new Hotel("h-mum-3", "Residency Hotel Fort", "mumbai", "Mid-Range", 5800, 4.4, "Fort Heritage District", new String[]{"Free WiFi", "Near CST Station"})
            },
            new Restaurant[] {
                new Restaurant("r-mum-1", "Britannia & Co. Restaurant", "mumbai", "Parsi", 1000, 4.6, "Berry Pulao & Sali Boti", "Ballard Estate"),
                new Restaurant("r-mum-2", "Trishna", "mumbai", "Seafood", 2600, 4.8, "Butter Garlic King Crab & Koliwada Prawns", "Kala Ghoda, Fort"),
                new Restaurant("r-mum-3", "Bademiya", "mumbai", "Street Food / Mughlai", 650, 4.3, "Mutton Seekh Kebab & Baida Roti", "Tulloch Road, Colaba")
            },
            new Transport[] {
                new Transport("t-mum-1", "mumbai", "Flight", "Chhatrapati Shivaji Maharaj International (BOM)", "Primary international and domestic hub", 4600, "Sahar & Santacruz terminals"),
                new Transport("t-mum-2", "mumbai", "Train", "CSMT & Mumbai Central", "Western & Central Railway lifelines", 900, "Lifeline suburban locals and long distance expresses"),
                new Transport("t-mum-3", "mumbai", "Local Transit", "Mumbai Suburban Local Trains & Coastal Road", "Reaches every corner from Churchgate to Dahanu", 25, "Inexpensive iconic double-decker electric buses and cabs")
            }
        ),

        // 8. Jaipur
        new Destination(
            "jaipur", "Jaipur", "Rajasthan", "Heritage",
            "The world-renowned Pink City and capital of Rajasthan. A UNESCO World Heritage city renowned for terracotta-pink stone facades, hillside Rajput forts, fairy-tale royal palaces, and vibrant block-printed textile bazaars.",
            "/images/dest_jaipur.jpg",
            26.9124, 75.7873, 3400, "October to March",
            new Attraction[] {
                new Attraction("jai-1", "Hawa Mahal (Palace of Winds)", "jaipur", "Heritage", "1799 pink sandstone five-story honeycomb palace with 953 ornate jharokhas (casements).", 50, 4.6, "09:00 AM - 05:00 PM", "/images/attr_jai-1.jpg"),
                new Attraction("jai-2", "Amber Fort & Palace", "jaipur", "Heritage", "Majestic hilltop fort built of yellow and pink sandstone with the sparkling Sheesh Mahal (Mirror Palace).", 100, 4.8, "08:00 AM - 05:30 PM", "/images/attr_jai-2.jpg"),
                new Attraction("jai-3", "City Palace & Jantar Mantar", "jaipur", "Heritage", "Palatial royal museum complex alongside the world's largest stone astronomical observatory.", 200, 4.7, "09:30 AM - 05:00 PM", "/images/attr_jai-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-jai-1", "Rambagh Palace", "jaipur", "Luxury", 42000, 4.9, "Bhawani Singh Road", new String[]{"Former Royal Residence", "Peacock Gardens", "Jiva Grande Spa"}),
                new Hotel("h-jai-2", "Samode Haveli", "jaipur", "Boutique Heritage", 17000, 4.8, "Gangapole, Old City", new String[]{"Frescoed Suites", "Courtyard Pool"}),
                new Hotel("h-jai-3", "Shahpura House", "jaipur", "Mid-Range", 5500, 4.5, "Bani Park", new String[]{"Traditional Rajput Hospitality", "Rooftop Restaurant"})
            },
            new Restaurant[] {
                new Restaurant("r-jai-1", "1135 AD @ Amber Fort", "jaipur", "Royal Rajasthani", 3000, 4.8, "Lal Maas & Dal Baati Churma in silver thalis", "Amber Fort premises"),
                new Restaurant("r-jai-2", "LMB (Laxmi Mishthan Bhandar)", "jaipur", "Rajasthani Vegetarian", 800, 4.5, "Rajasthani Royal Thali & Ghewar", "Johari Bazaar"),
                new Restaurant("r-jai-3", "Rawat Mishthan Bhandar", "jaipur", "Snacks", 300, 4.6, "Crisp Pyaaz Ki Kachori & Mawa Kachori", "Station Road, Sindhi Camp")
            },
            new Transport[] {
                new Transport("t-jai-1", "jaipur", "Flight", "Jaipur International Airport (JAI)", "Direct domestic flights to all metros & Gulf connections", 3600, "Sanganer, 13km south"),
                new Transport("t-jai-2", "jaipur", "Train", "Jaipur Junction (JP)", "High-speed Vande Bharat and Shatabdi from Delhi in 3.5 hrs", 750, "North Western Railway hub"),
                new Transport("t-jai-3", "jaipur", "Road", "Delhi-Jaipur Expressway (NH 48 / NE 4)", "4-lane access controlled highway", 500, "Buses and private cabs every 15 mins")
            }
        ),

        // 9. Agra
        new Destination(
            "agra", "Agra", "Uttar Pradesh", "Heritage",
            "Home to the peerless Taj Mahal, Agra sits serenely on the banks of the Yamuna River. A former Mughal imperial capital, it hosts three UNESCO World Heritage Sites showcasing monumental Mughal art, red sandstone architecture, and marble inlay.",
            "/images/dest_agra.jpg",
            27.1767, 78.0081, 2800, "October to March",
            new Attraction[] {
                new Attraction("agr-1", "Taj Mahal", "agra", "Heritage", "The world's greatest monument of love. Pure white ivory marble mausoleum built by Mughal Emperor Shah Jahan.", 50, 4.9, "Sunrise to Sunset (Closed Fridays)", "/images/attr_agr-1.jpg"),
                new Attraction("agr-2", "Agra Fort", "agra", "Heritage", "Imperial red sandstone citadel that served as the primary residence of the Mughal dynasty emperors until 1638.", 50, 4.7, "06:00 AM - 06:00 PM", "/images/attr_agr-2.jpg"),
                new Attraction("agr-3", "Fatehpur Sikri", "agra", "Heritage", "Akbar's ghost city of red sandstone with the colossal 54-meter Buland Darwaza gate.", 50, 4.6, "06:00 AM - 06:00 PM", "/images/attr_agr-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-agr-1", "The Oberoi Amarvilas", "agra", "Luxury", 40000, 5.0, "Taj East Gate Road", new String[]{"Unobstructed Taj Mahal Views from Every Room", "Private Butler", "Pool"}),
                new Hotel("h-agr-2", "ITC Mughal Resort & Spa", "agra", "Luxury", 12000, 4.7, "Fatehabad Road", new String[]{"35-Acre Mughal Gardens", "Kaya Kalp Spa"}),
                new Hotel("h-agr-3", "Crystal Sarovar Premiere", "agra", "Mid-Range", 4500, 4.3, "Fatehabad Road", new String[]{"Rooftop Taj View Pool", "Restaurant"})
            },
            new Restaurant[] {
                new Restaurant("r-agr-1", "Peshawri @ ITC Mughal", "agra", "North Indian", 3800, 4.8, "Tandoori Jhinga & Dal Bukhara", "Fatehabad Road"),
                new Restaurant("r-agr-2", "Pinch of Spice", "agra", "North Indian & Mughlai", 1300, 4.6, "Murgh Tikka Butter Masala & Rogan Josh", "Wazirpura Road"),
                new Restaurant("r-agr-3", "Panchi Petha Store", "agra", "Sweets", 300, 4.7, "Original Kesar & Angoori Agra Petha", "Sadar Bazaar")
            },
            new Transport[] {
                new Transport("t-agr-1", "agra", "Train", "Agra Cantt (AGC)", "Gatimaan Express from Delhi in just 1 hour 40 minutes", 650, "Frequent fast intercity trains"),
                new Transport("t-agr-2", "agra", "Road", "Yamuna Expressway (6-lane access controlled)", "Smooth 2.5 hour highway drive from New Delhi", 450, "Express Volvo buses and taxis"),
                new Transport("t-agr-3", "agra", "Flight", "Agra Kheria Airport (AGR)", "Direct connections to Lucknow, Mumbai, and Jaipur", 3500, "Civil enclave air terminal")
            }
        ),

        // 10. Kochi
        new Destination(
            "kochi", "Kochi", "Kerala", "Cultural",
            "The Queen of the Arabian Sea, Kochi is a charming historic spice port where centuries of Chinese, Portuguese, Dutch, and British cultural influences converge along tranquil backwater estuaries and coconut palm groves.",
            "/images/dest_kochi.jpg",
            9.9312, 76.2673, 3200, "October to March",
            new Attraction[] {
                new Attraction("koc-1", "Chinese Fishing Nets", "kochi", "Cultural", "Cantilevered shore-operated spider-like fishing nets introduced by Chinese explorer Zheng He in the 14th century.", 0, 4.6, "Active during sunrise & sunset", "/images/attr_koc-1.jpg"),
                new Attraction("koc-2", "Mattancherry Dutch Palace & Jew Town", "kochi", "Heritage", "1555 Portuguese palace featuring vibrant Ramayana mural paintings and ancient 1568 Paradesi Synagogue.", 20, 4.5, "10:00 AM - 05:00 PM (Fridays Closed)", "/images/attr_koc-2.jpg"),
                new Attraction("koc-3", "Fort Kochi Colonial Promenade", "kochi", "Heritage", "Tree-shaded cobbled lanes lined with Dutch bungalows, St. Francis Church, and artisan coffee cafes.", 0, 4.7, "Open 24 Hours", "/images/attr_koc-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-koc-1", "Brunton Boatyard", "kochi", "Luxury Heritage", 22000, 4.8, "Fort Kochi Pier", new String[]{"Historical Shipyard Aesthetic", "Harbour View", "Ayurveda"}),
                new Hotel("h-koc-2", "Grand Hyatt Kochi Bolgatty", "kochi", "Luxury", 15000, 4.8, "Bolgatty Island", new String[]{"Waterfront Marina", "Spa", "Lakeside Pools"}),
                new Hotel("h-koc-3", "Forte Kochi Heritage Hotel", "kochi", "Boutique", 7500, 4.6, "Princess Street, Fort Kochi", new String[]{"Colonial Architecture", "Courtyard Pool"})
            },
            new Restaurant[] {
                new Restaurant("r-koc-1", "Malabar Junction @ Malabar House", "kochi", "Kerala Fusion", 2400, 4.7, "Tiger Prawns in Coconut & Mango Curry", "Parade Ground, Fort Kochi"),
                new Restaurant("r-koc-2", "Fusion Bay", "kochi", "Seafood", 1100, 4.6, "Karimeen Pollichathu (Pearl Spot in Banana Leaf)", "KB Jacob Road"),
                new Restaurant("r-koc-3", "Kashi Art Cafe", "kochi", "Cafe", 700, 4.5, "Cold Brew Coffee & Fresh Banana Chocolate Cake", "Burgher Street")
            },
            new Transport[] {
                new Transport("t-koc-1", "kochi", "Flight", "Cochin International Airport (COK)", "World's first 100% solar-powered airport", 4300, "Nedumbassery, 28km north"),
                new Transport("t-koc-2", "kochi", "Water Metro", "Kochi Water Metro", "Eco-friendly electric battery-powered passenger ferry network", 30, "Connects 10 scenic backwater islands"),
                new Transport("t-koc-3", "kochi", "Train", "Ernakulam Junction (ERS) & Town (ERN)", "Hub for southern railways to Trivandrum & Chennai", 750, "Frequent coastal express connections")
            }
        ),

        // 11. Mysuru
        new Destination(
            "mysuru", "Mysuru", "Karnataka", "Heritage",
            "The cultural capital of Karnataka and city of palaces. Renowned for the luminous golden Mysore Palace, vibrant Dasara royal pageantry, sweet Mysore Pak delicacies, and fragrant sandalwood and silk weaving heritage.",
            "/images/dest_mysuru.jpg",
            12.2958, 76.6394, 2600, "October to March",
            new Attraction[] {
                new Attraction("mys-1", "Mysore Palace (Amba Vilas)", "mysuru", "Heritage", "Indo-Saracenic royal palace with stained glass, golden throne, illuminated with 97,000 electric bulbs on Sundays.", 100, 4.8, "10:00 AM - 05:30 PM", "/images/attr_mys-1.jpg"),
                new Attraction("mys-2", "Chamundi Hill & Temple", "mysuru", "Spiritual", "Hilltop temple at 1,000 meters dedicated to Goddess Chamundeshwari with a monolithic 5m Nandi bull statue.", 0, 4.6, "07:30 AM - 09:00 PM", "/images/attr_mys-2.jpg"),
                new Attraction("mys-3", "Brindavan Gardens & KRS Dam", "mysuru", "Nature", "Terraced Mughal-style garden famous for its evening illuminated dancing musical fountains.", 50, 4.3, "06:00 AM - 08:00 PM", "/images/attr_mys-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-mys-1", "Lalitha Mahal Palace Hotel", "mysuru", "Heritage Luxury", 14000, 4.6, "Chamundi Hill Road", new String[]{"Italian Marble Dome", "Billiard Room", "Heritage Banquet"}),
                new Hotel("h-mys-2", "Radisson Blu Plaza Hotel", "mysuru", "Luxury", 8500, 4.7, "MG Road, Nazarbad", new String[]{"Spa", "Pool", "Chamundi View"}),
                new Hotel("h-mys-3", "Southern Star Mysuru", "mysuru", "Mid-Range", 4200, 4.3, "Vinobha Road", new String[]{"Central Location", "Pool", "Gardens"})
            },
            new Restaurant[] {
                new Restaurant("r-mys-1", "Mylari Hotel (Original Vinayaka)", "mysuru", "South Indian", 250, 4.8, "Mylari Butter Dosa with Coconut & Coriander Chutney", "Nazarbad Main Road"),
                new Restaurant("r-mys-2", "Guru Sweet Mart", "mysuru", "Sweets", 200, 4.9, "Authentic Melt-in-Mouth Royal Mysore Pak", "Sayyaji Rao Road"),
                new Restaurant("r-mys-3", "RRR Hotel", "mysuru", "Andhra & Mysore", 600, 4.5, "Spicy Mutton Biryani on Banana Leaf", "Gandhi Square")
            },
            new Transport[] {
                new Transport("t-mys-1", "mysuru", "Train", "Mysuru Junction (MYS)", "Vande Bharat connecting to Bengaluru in 1 hr 45 min", 500, "Frequent scenic trains to Bengaluru"),
                new Transport("t-mys-2", "mysuru", "Road", "Bengaluru-Mysuru 10-lane Expressway", "Quick 90 minute highway drive from Bangalore", 350, "KSRTC Flybus and express services"),
                new Transport("t-mys-3", "mysuru", "Flight", "Mysore Airport (MYQ)", "Regional flights connecting Goa, Hyderabad, and Chennai", 3200, "Mandakalli, 10km south")
            }
        ),

        // 12. Pune
        new Destination(
            "pune", "Pune", "Maharashtra", "Cultural",
            "The cultural capital of Maharashtra and the historic seat of the Maratha Peshwas. A thriving university hub and technology city nestled against the misty Sahyadri hills, celebrated for grand forts, universities, and bakery culture.",
            "/images/dest_pune.jpg",
            18.5204, 73.8567, 3100, "July to February",
            new Attraction[] {
                new Attraction("pun-1", "Shaniwar Wada", "pune", "Heritage", "1732 seven-story fortified palace headquarters of the Maratha Empire Peshwas.", 25, 4.5, "09:30 AM - 05:30 PM", "/images/attr_pun-1.jpg"),
                new Attraction("pun-2", "Aga Khan Palace", "pune", "Heritage", "Italian arches and manicured lawns where Mahatma Gandhi was interned during the Quit India movement.", 25, 4.6, "09:00 AM - 05:30 PM", "/images/attr_pun-2.jpg"),
                new Attraction("pun-3", "Sinhagad Fort", "pune", "Adventure", "Hilltop fortress set at 1,312 meters with sweeping views of the Sahyadri ranges, site of Tanaji's historic 1670 battle.", 50, 4.7, "06:00 AM - 06:00 PM", "/images/attr_pun-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-pun-1", "JW Marriott Hotel Pune", "pune", "Luxury", 14000, 4.8, "Senapati Bapat Road", new String[]{"Rooftop Lounge", "Spa", "Multiple Dining"}),
                new Hotel("h-pun-2", "The Ritz-Carlton Pune", "pune", "Luxury", 18000, 4.9, "Golf Course Road, Yerwada", new String[]{"Golf View", "Signature Suites"}),
                new Hotel("h-pun-3", "Royal Orchid Central", "pune", "Mid-Range", 4500, 4.3, "Kalyani Nagar", new String[]{"Fitness Center", "WiFi"})
            },
            new Restaurant[] {
                new Restaurant("r-pun-1", "Kayani Bakery", "pune", "Bakery", 350, 4.7, "World-Famous Shrewsbury Biscuits & Mawa Cake", "East Street, Camp"),
                new Restaurant("r-pun-2", "Vaishali Restaurant", "pune", "South Indian & Snacks", 500, 4.6, "Special SPDP (Sev Potato Dahi Puri) & Mysore Dosa", "FC Road"),
                new Restaurant("r-pun-3", "Maratha Samrat", "pune", "Maharashtrian", 1100, 4.7, "Spicy Mutton Thali & Tambda Rassa", "Baner & Camp")
            },
            new Transport[] {
                new Transport("t-pun-1", "pune", "Flight", "Pune International Airport (PNQ)", "Direct connectivity to all key domestic metros", 3800, "Lohegaon, 10km northeast"),
                new Transport("t-pun-2", "pune", "Road", "Mumbai-Pune Expressway (India's First 6-lane)", "Superfast 2.5 hour road link from Mumbai", 450, "Frequent Shivneri AC buses and cabs"),
                new Transport("t-pun-3", "pune", "Train", "Pune Junction (PUNE)", "Origin of Deccan Queen and Vande Bharat to Mumbai", 550, "Frequent intercity trains")
            }
        ),

        // 13. Udaipur
        new Destination(
            "udaipur", "Udaipur", "Rajasthan", "Heritage",
            "The City of Lakes and the Venice of the East. Majestic whitewashed Rajput palaces reflect peacefully upon the shimmer of Lake Pichola, framed by the rugged purple peaks of the ancient Aravalli Hills.",
            "/images/dest_udaipur.jpg",
            24.5854, 73.7125, 4200, "September to March",
            new Attraction[] {
                new Attraction("uda-1", "City Palace Udaipur", "udaipur", "Heritage", "Rajasthan's largest palace complex featuring peacock courtyards, mirror mosaics, and royal vintage collections.", 300, 4.8, "09:30 AM - 05:30 PM", "/images/attr_uda-1.jpg"),
                new Attraction("uda-2", "Lake Pichola & Jag Mandir", "udaipur", "Scenic", "Romantic freshwater lake with fairy-tale island palaces reached by royal motorboat cruises.", 400, 4.9, "Boat rides 09:00 AM - 06:00 PM", "/images/attr_uda-2.jpg"),
                new Attraction("uda-3", "Saheliyon-ki-Bari", "udaipur", "Nature", "Historic Courtyard of the Maidens with lotus pools, marble pavilions, and natural pressure fountains.", 50, 4.5, "09:00 AM - 07:00 PM", "/images/attr_uda-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-uda-1", "Taj Lake Palace", "udaipur", "Luxury Heritage", 45000, 5.0, "Island in Lake Pichola", new String[]{"Floating Island Palace", "Jharokha Dining", "Heritage Escort"}),
                new Hotel("h-uda-2", "The Oberoi Udaivilas", "udaipur", "Ultra Luxury", 52000, 5.0, "Haridas Ji Ki Magri", new String[]{"Semi-Private Moat Pools", "Peacock Sanctuaries", "Spa"}),
                new Hotel("h-uda-3", "Jagat Niwas Palace", "udaipur", "Boutique Heritage", 8500, 4.7, "Lal Ghat", new String[]{"Lakeside Jharokhas", "Rooftop Dining"})
            },
            new Restaurant[] {
                new Restaurant("r-uda-1", "Ambrai Restaurant @ Amet Haveli", "udaipur", "North Indian & Mughlai", 2400, 4.8, "Mewari Mutton & Fresh Naan with Lake Pichola Panorama", "Outside Chandpole"),
                new Restaurant("r-uda-2", "Upre by 1559 AD", "udaipur", "Rooftop Multi-Cuisine", 2000, 4.7, "Lal Maas with Views of Illuminated City Palace", "Lake Pichola Hotel Terrace"),
                new Restaurant("r-uda-3", "Jheel's Ginger Coffee Bar", "udaipur", "Bakery & Cafe", 600, 4.6, "Wood-fired Pizza & Apple Pie by the Water", "Gangaur Ghat")
            },
            new Transport[] {
                new Transport("t-uda-1", "udaipur", "Flight", "Maharana Pratap Airport (UDR)", "Daily direct flights from Delhi, Mumbai, Bengaluru, Jaipur", 4200, "Dabok, 22km east"),
                new Transport("t-uda-2", "udaipur", "Train", "Udaipur City Railway Station (UDZ)", "Direct overnight expresses to Delhi, Jaipur, and Mumbai", 850, "Well connected to North India"),
                new Transport("t-uda-3", "udaipur", "Boat", "Lake Pichola Municipal & Private Boat Cruises", "Scenic transfers between ghats and island pavilions", 350, "Board from Rameshwar Ghat / City Palace")
            }
        ),

        // 14. Rishikesh
        new Destination(
            "rishikesh", "Rishikesh", "Uttarakhand", "Spiritual",
            "The Yoga Capital of the World and gateway to the Garhwal Himalayas. Sacred emerald waters of the Ganga rush past ancient ashrams, iron suspension bridges, yoga sanctuaries, and thrilling whitewater river rafting rapids.",
            "/images/dest_rishikesh.jpg",
            30.0869, 78.2676, 2400, "September to April",
            new Attraction[] {
                new Attraction("ris-1", "Lakshman Jhula & Ram Jhula", "rishikesh", "Scenic", "Historic iron suspension bridges spanning the turquoise Ganga with sweeping Himalayan views.", 0, 4.7, "Open 24 Hours", "/images/attr_ris-1.jpg"),
                new Attraction("ris-2", "Triveni Ghat Evening Aarti", "rishikesh", "Spiritual", "Sacred confluence of three holy rivers where chanting priests and floating leaf diya lamps illuminate the waters.", 0, 4.8, "06:00 PM Daily", "/images/attr_ris-2.jpg"),
                new Attraction("ris-3", "Whitewater River Rafting & Camping", "rishikesh", "Adventure", "Grade III and IV thrilling rapids (The Wall, Roller Coaster) over 16km or 24km river stretches.", 1200, 4.9, "08:00 AM - 04:00 PM (Oct-Jun)", "/images/attr_ris-3.jpg"),
                new Attraction("ris-4", "The Beatles Ashram (Chaurasi Kutia)", "rishikesh", "Culture", "Historic ashram where the Beatles stayed in 1968 and composed the White Album.", 150, 4.6, "09:00 AM - 04:00 PM", "/images/attr_ris-4.jpg")
            },
            new Hotel[] {
                new Hotel("h-ris-1", "Ananda in the Himalayas", "rishikesh", "Wellness Luxury", 48000, 5.0, "Narendra Nagar Palace Estate", new String[]{"World-Ranked Ayurvedic Wellness Spa", "Meditation Pavilions"}),
                new Hotel("h-ris-2", "Taj Rishikesh Resort & Spa", "rishikesh", "Luxury", 24000, 4.9, "Singthali, Riverfront", new String[]{"Private River Beach", "Himalayan View"}),
                new Hotel("h-ris-3", "Zostel Rishikesh (Tapovan)", "rishikesh", "Budget", 1500, 4.5, "Tapovan", new String[]{"Yoga Terrace", "Cafe", "Travel Desk"})
            },
            new Restaurant[] {
                new Restaurant("r-ris-1", "Chotiwala Restaurant", "rishikesh", "Pure Vegetarian", 450, 4.4, "Traditional Garhwali Thali & Fresh Lassi", "Swarg Ashram"),
                new Restaurant("r-ris-2", "Beatles Cafe (The 60's Cafe)", "rishikesh", "Organic & Continental", 850, 4.7, "Gluten-Free Pancakes & Ganga View Smoothies", "Paidal Marg, Tapovan"),
                new Restaurant("r-ris-3", "Little Buddha Cafe", "rishikesh", "Multi-Cuisine Cafe", 700, 4.6, "Hummus Falafel Platter & Herbal Teas", "Lakshman Jhula Road")
            },
            new Transport[] {
                new Transport("t-ris-1", "rishikesh", "Train", "Yog Nagari Rishikesh (YNRK)", "Modern Himalayan rail terminal with direct trains to Delhi & Jammu", 550, "Newly commissioned terminal"),
                new Transport("t-ris-2", "rishikesh", "Flight", "Dehradun Jolly Grant Airport (DED)", "Closest airport just 20km from Rishikesh", 3600, "Daily flights to Delhi, Mumbai, Bengaluru"),
                new Transport("t-ris-3", "rishikesh", "Road", "Delhi-Haridwar-Rishikesh Highway", "Smooth 5 hour drive from New Delhi", 400, "AC buses and shared cabs")
            }
        ),

        // 15. Kolkata
        new Destination(
            "kolkata", "Kolkata", "West Bengal", "Cultural",
            "The City of Joy and India's intellectual and cultural powerhouse. Famed for grand British Raj colonial architecture, the mighty Howrah cantilever bridge, tram cars, literary coffee houses, and divine Bengali sweets.",
            "/images/dest_kolkata.jpg",
            22.5726, 88.3639, 2800, "October to March",
            new Attraction[] {
                new Attraction("kol-1", "Victoria Memorial", "kolkata", "Heritage", "Majestic white Makrana marble monument set in 64 acres of gardens dedicated to Queen Victoria.", 50, 4.8, "10:00 AM - 05:00 PM (Gardens 05:30 AM - 06:00 PM)", "/images/attr_kol-1.jpg"),
                new Attraction("kol-2", "Howrah Bridge (Rabindra Setu)", "kolkata", "Heritage", "Massive 705-meter cantilever steel suspension bridge over the Hooghly River with zero nuts and bolts.", 0, 4.7, "Open 24 Hours", "/images/attr_kol-2.jpg"),
                new Attraction("kol-3", "Dakshineswar Kali Temple", "kolkata", "Spiritual", "Navaratna architectural 1855 temple on the river bank associated with mystic Ramakrishna Paramahamsa.", 0, 4.8, "06:00 AM - 12:30 PM, 03:00 PM - 08:30 PM", "/images/attr_kol-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-kol-1", "The Oberoi Grand Kolkata", "kolkata", "Luxury Heritage", 16000, 4.8, "Jawaharlal Nehru Road, Esplanade", new String[]{"Grand Dame of the East", "Colonial Courtyard Pool", "Spa"}),
                new Hotel("h-kol-2", "ITC Sonar & Royal Bengal", "kolkata", "Luxury", 14000, 4.9, "JBS Haldane Avenue, EM Bypass", new String[]{"Pond Pavilions", "Michelin-caliber Bengali Cuisine"}),
                new Hotel("h-kol-3", "The Peerless Inn", "kolkata", "Mid-Range", 4800, 4.3, "Chowringhee Road", new String[]{"Central Location", "Aaheli Bengali Restaurant"})
            },
            new Restaurant[] {
                new Restaurant("r-kol-1", "Peter Cat", "kolkata", "Continental & Indian", 1200, 4.7, "Legendary Chelo Kebab & Baked Alaska", "Park Street"),
                new Restaurant("r-kol-2", "6 Ballygunge Place", "kolkata", "Authentic Bengali", 1400, 4.8, "Daab Chingri (Prawns in Coconut) & Kosha Mangsho", "Ballygunge"),
                new Restaurant("r-kol-3", "K.C. Das & Girish Chandra Dey", "kolkata", "Sweets", 250, 4.9, "Hot Spongy Rosogolla & Nolen Gur Sandesh", "Esplanade & Hatibagan")
            },
            new Transport[] {
                new Transport("t-kol-1", "kolkata", "Flight", "Netaji Subhash Chandra Bose International (CCU)", "Key hub for Eastern India and Southeast Asia", 4100, "Dum Dum, 16km northeast"),
                new Transport("t-kol-2", "kolkata", "Metro", "Kolkata Metro (India's Oldest & Under-River Line)", "Features India's first underwater subway under the Hooghly", 35, "Clean, efficient transit"),
                new Transport("t-kol-3", "kolkata", "Train", "Howrah Junction (HWH) & Sealdah (SDAH)", "India's largest and busiest railway terminal with 23 platforms", 950, "Hub for Eastern and South Eastern Railways")
            }
        ),

        // 16. Amritsar
        new Destination(
            "amritsar", "Amritsar", "Punjab", "Spiritual",
            "The spiritual sanctuary of Sikhism, Amritsar is anchored by the dazzling Golden Temple (Sri Harmandir Sahib). Renowned for its unparalleled communal hospitality, historic patriotism, and world-famous Punjabi culinary feast.",
            "/images/dest_amritsar.jpg",
            31.6340, 74.8723, 2700, "October to March",
            new Attraction[] {
                new Attraction("amr-1", "Sri Harmandir Sahib (Golden Temple)", "amritsar", "Spiritual", "Breathtaking gold-leaf sanctum floating amidst the sacred Amrit Sarovar pool, serving free langar to 100,000 pilgrims daily.", 0, 5.0, "Open 24 Hours", "/images/attr_amr-1.jpg"),
                new Attraction("amr-2", "Jallianwala Bagh Memorial", "amritsar", "Heritage", "Sacred national memorial honoring the victims of the 1919 massacre, retaining bullet-marked walls and the historic well.", 0, 4.7, "06:30 AM - 07:30 PM", "/images/attr_amr-2.jpg"),
                new Attraction("amr-3", "Attari-Wagah Border Ceremony", "amritsar", "Patriotic", "Electrifying daily military flag-lowering retreat parade between Indian BSF and Pakistan Rangers.", 0, 4.8, "04:30 PM - 06:00 PM (30km from city)", "/images/attr_amr-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-amr-1", "Taj Swarna Amritsar", "amritsar", "Luxury", 11000, 4.8, "Majitha Verka Bypass", new String[]{"Outdoor Pool", "Spa", "Langar Shuttle"}),
                new Hotel("h-amr-2", "Hyatt Regency Amritsar", "amritsar", "Luxury", 9500, 4.7, "MBM Farms, GT Road", new String[]{"Vitality Spa", "Pool", "Punjabi Cuisine"}),
                new Hotel("h-amr-3", "Ramada Amritsar", "amritsar", "Mid-Range", 4800, 4.4, "Hall Bazaar near Golden Temple", new String[]{"Rooftop Pool", "Walking to Sanctum"})
            },
            new Restaurant[] {
                new Restaurant("r-amr-1", "Kesar Da Dhaba (Since 1916)", "amritsar", "Punjabi Dhaba", 550, 4.7, "Slow-cooked Maa Ki Dal & Lacha Paratha in Pure Ghee", "Chowk Passian"),
                new Restaurant("r-amr-2", "Bhai Kulwant Singh Kulchian Wale", "amritsar", "Punjabi", 300, 4.8, "Crisp Tandoori Amritsari Aloo Kulcha & Chole", "Near Golden Temple"),
                new Restaurant("r-amr-3", "Ahuja Milk Bhandar", "amritsar", "Dessert", 150, 4.8, "Thick Malai Kesar Lassi", "Dhab Khatikan")
            },
            new Transport[] {
                new Transport("t-amr-1", "amritsar", "Flight", "Sri Guru Ram Dass Jee International (ATQ)", "Direct flights to UK, Gulf, Delhi, and Mumbai", 3700, "Rajasansi, 11km northwest"),
                new Transport("t-amr-2", "amritsar", "Train", "Amritsar Junction (ASR)", "High-speed Vande Bharat and Shatabdi link to New Delhi in 5.5 hours", 750, "Major Northern Railway terminus"),
                new Transport("t-amr-3", "amritsar", "Local Transit", "Free Golden Temple Electric Shuttles & E-rickshaws", "Zero-pollution transit in pedestrian heritage zone", 20, "Runs 24/7 around the sacred zone")
            }
        ),

        // 17. Ooty
        new Destination(
            "ooty", "Ooty (Udhagamandalam)", "Tamil Nadu", "Mountain",
            "The Queen of Nilgiri Hill Stations. Perched at 2,240 meters amidst rolling emerald tea estates, aromatic eucalyptus plantations, picturesque British stone cottages, and the historic UNESCO Nilgiri Mountain Toy Train.",
            "/images/dest_ooty.jpg",
            11.4102, 76.6950, 3100, "March to June & September to November",
            new Attraction[] {
                new Attraction("oot-1", "Nilgiri Mountain Railway (Toy Train)", "ooty", "Heritage", "UNESCO World Heritage 1908 rack-and-pinion steam locomotive winding through tunnels and dramatic ravines.", 205, 4.8, "Departures from Mettupalayam & Ooty", "/images/attr_oot-1.jpg"),
                new Attraction("oot-2", "Ooty Botanical Gardens", "ooty", "Nature", "55-acre terraced garden established in 1848 with a 20-million-year-old fossilized tree trunk.", 40, 4.5, "07:00 AM - 06:30 PM", "/images/attr_oot-2.jpg"),
                new Attraction("oot-3", "Doddabetta Peak", "ooty", "Scenic", "Highest point in the Nilgiri Hills at 2,637 meters offering sweeping panoramic views of the Western Ghats.", 30, 4.6, "09:00 AM - 06:00 PM", "/images/attr_oot-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-oot-1", "Savoy - IHCL SeleQtions", "ooty", "Luxury Heritage", 18000, 4.8, "Sylks Road", new String[]{"180-Year-Old English Countryside Manse", "Fireplace in Rooms"}),
                new Hotel("h-oot-2", "Ferrnhills Royale Palace", "ooty", "Heritage Luxury", 16000, 4.7, "Ferrnhills Post", new String[]{"Former Summer Palace of Mysore Maharajas", "Lush Woods"}),
                new Hotel("h-oot-3", "Sinclairs Retreat Ooty", "ooty", "Mid-Range", 5500, 4.4, "Gorishola Road", new String[]{"Hilltop Valley Views", "Garden Restaurant"})
            },
            new Restaurant[] {
                new Restaurant("r-oot-1", "Earl's Secret @ King's Cliff", "ooty", "Continental & Anglo-Indian", 1600, 4.7, "Steaks & Pasta in Glasshouse Conservatory", "Havelock Road"),
                new Restaurant("r-oot-2", "Nahar's Sidewalk Cafe", "ooty", "Italian & Cafe", 850, 4.5, "Wood-fired Margherita Pizza & Nilgiri Tea", "Commercial Road"),
                new Restaurant("r-oot-3", "King Star Handmade Chocolates", "ooty", "Confectionery", 300, 4.8, "Fudge, Truffles & Roasted Almond Nilgiri Chocolate", "Commercial Street")
            },
            new Transport[] {
                new Transport("t-oot-1", "ooty", "Train", "Nilgiri Mountain Railway (Toy Train)", "Scenic heritage railway journey from Mettupalayam", 205, "Daily morning toy train service"),
                new Transport("t-oot-2", "ooty", "Road", "Coimbatore-Ooty Hill Highway (36 hairpin bends)", "Spectacular 3 hour scenic mountain ascent", 450, "Regular buses and private cabs"),
                new Transport("t-oot-3", "ooty", "Flight", "Coimbatore International Airport (CJB)", "Nearest commercial airport 88km downhill", 3500, "Direct connections to Chennai, Mumbai, Bangalore")
            }
        ),

        // 18. Manali
        new Destination(
            "manali", "Manali", "Himachal Pradesh", "Mountain",
            "High-altitude mountain wonderland in the Beas River Valley. Framed by snow-capped Pir Panjal and Dhauladhar Himalayan peaks, fragrant pine forests, roaring river rapids, and high-altitude mountain passes.",
            "/images/dest_manali.jpg",
            32.2432, 77.1892, 3300, "October to June (Snow in Dec-Feb)",
            new Attraction[] {
                new Attraction("man-1", "Solang Valley & Rohtang Pass", "manali", "Adventure", "Premier snow sports destination for skiing, paragliding, snowmobiles at 3,978 meters elevation.", 500, 4.8, "06:00 AM - 04:00 PM (Rohtang permits required)", "/images/attr_man-1.jpg"),
                new Attraction("man-2", "Hadimba Temple", "manali", "Heritage", "1553 four-tiered wooden pagoda temple set inside towering deodar cedar forests.", 20, 4.6, "08:00 AM - 06:00 PM", "/images/attr_man-2.jpg"),
                new Attraction("man-3", "Atal Tunnel & Sissu (Lahaul)", "manali", "Scenic", "World's longest highway tunnel above 10,000 feet (9.02 km) leading to the dramatic Lahaul valley.", 0, 4.9, "Open year-round", "/images/attr_man-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-man-1", "The Himalayan Luxury Resort", "manali", "Luxury", 19000, 4.8, "Hadimba Road", new String[]{"Victorian Gothic Castle", "Thermal Pool", "Cherry Orchards"}),
                new Hotel("h-man-2", "Span Resort & Spa", "manali", "Luxury Riverfront", 22000, 4.9, "Baragarh, Beas Riverfront", new String[]{"Private Beas River Walk", "Heli-Skiing", "Spa"}),
                new Hotel("h-man-3", "Johnson Lodge & Spa", "manali", "Mid-Range", 5800, 4.5, "Circuit House Road", new String[]{"Fireplace", "Bar & Grill", "Pine Wood Rooms"})
            },
            new Restaurant[] {
                new Restaurant("r-man-1", "The Johnson's Cafe", "manali", "Trout & Continental", 1300, 4.7, "Pan-fried Fresh Himalayan River Trout & Cider", "Circuit House Road"),
                new Restaurant("r-man-2", "Cafe 1947", "manali", "Italian & Live Music", 1100, 4.6, "Wood-fired Pizza next to rushing mountain stream", "Old Manali"),
                new Restaurant("r-man-3", "Drifters' Cafe", "manali", "Tibetan & Continental", 750, 4.5, "Steamed Momos, Thukpa & Apple Crumble", "Manu Temple Road, Old Manali")
            },
            new Transport[] {
                new Transport("t-man-1", "manali", "Road", "Delhi-Chandigarh-Manali 4-Lane Highway", "Overnight Volvo bus luxury transit from New Delhi (11 hours)", 1200, "Daily luxury sleeper and AC coaches"),
                new Transport("t-man-2", "manali", "Flight", "Bhuntar Kullu Airport (KUU)", "Nearest airport 50km south along the Beas river", 4800, "Daily flights connecting Delhi and Chandigarh"),
                new Transport("t-man-3", "manali", "Local Transit", "4x4 Mountain Jeeps & Snow Cabs", "Handles high mountain terrain and Rohtang pass", 1500, "Available at Mall Road taxi stand")
            }
        ),

        // 19. Darjeeling
        new Destination(
            "darjeeling", "Darjeeling", "West Bengal", "Mountain",
            "The Champagne of Teas and Queen of the Hills. Famous for misty Himalayan views of Mount Kanchenjunga (world's 3rd highest peak), world-renowned muscatel tea gardens, and the century-old UNESCO Darjeeling Himalayan Toy Train.",
            "/images/dest_darjeeling.jpg",
            27.0410, 88.2663, 2900, "March to May & October to December",
            new Attraction[] {
                new Attraction("dar-1", "Tiger Hill Sunrise Viewpoint", "darjeeling", "Scenic", "Celebrated sunrise viewpoint casting golden and amber light across the snowfields of Mt. Kanchenjunga and Mt. Everest.", 50, 4.8, "04:00 AM - 06:30 AM", "/images/attr_dar-1.jpg"),
                new Attraction("dar-2", "Darjeeling Himalayan Railway (Toy Train)", "darjeeling", "Heritage", "UNESCO World Heritage 1881 two-foot narrow gauge mountain steam train negotiating the Batasia Loop spiral.", 1000, 4.9, "Joy rides throughout day", "/images/attr_dar-2.jpg"),
                new Attraction("dar-3", "Happy Valley Tea Estate", "darjeeling", "Nature", "Historic 1854 biodynamic tea estate producing the finest first-flush aromatic muscatel teas.", 100, 4.7, "09:00 AM - 04:30 PM", "/images/attr_dar-3.jpg")
            },
            new Hotel[] {
                new Hotel("h-dar-1", "The Elgin, Darjeeling", "darjeeling", "Heritage Luxury", 14000, 4.8, "HD Lama Road", new String[]{"Former Summer Residence of Maharaja of Cooch Behar", "Fireplace"}),
                new Hotel("h-dar-2", "Windamere Hotel", "darjeeling", "Historic Colonial", 16000, 4.7, "Observatory Hill", new String[]{"British Colonial Ambiance", "Afternoon High Tea"}),
                new Hotel("h-dar-3", "Cedar Inn", "darjeeling", "Mid-Range", 6500, 4.4, "Jalapahar Road", new String[]{"Wood Interiors", "Kanchenjunga Panorama"})
            },
            new Restaurant[] {
                new Restaurant("r-dar-1", "Glenary's Bakery & Restaurant (Since 1910)", "darjeeling", "Bakery & European", 800, 4.8, "Apple Strudel, Cinnamon Buns & Darjeeling First Flush Tea", "Nehru Road, Mall"),
                new Restaurant("r-dar-2", "Keventers (Since 1911)", "darjeeling", "Breakfast & Anglo-Indian", 600, 4.6, "Full English Breakfast & Hot Chocolate on the Open Deck", "Club Side, Nehru Road"),
                new Restaurant("r-dar-3", "Kunga Restaurant", "darjeeling", "Tibetan", 450, 4.7, "Steaming Beef Gyathuk & Steamed Pork Momos", "Gandhi Road")
            },
            new Transport[] {
                new Transport("t-dar-1", "darjeeling", "Flight", "Bagdogra Airport (IXB)", "Nearest commercial airport 70km south in Siliguri", 3900, "Connects Kolkata, Delhi, Bangalore, Guwahati"),
                new Transport("t-dar-2", "darjeeling", "Train", "New Jalpaiguri (NJP)", "Gateway railway station connecting eastern India and North East", 850, "Vande Bharat from Kolkata in 7.5 hours"),
                new Transport("t-dar-3", "darjeeling", "Road", "Siliguri-Darjeeling Hill Road (NH 110)", "Picturesque 3 hour mountain climb through tea estates", 400, "Shared and private mountain jeeps")
            }
        ),

        // 20. Visakhapatnam
        new Destination(
            "visakhapatnam", "Visakhapatnam (Vizag)", "Andhra Pradesh", "Beach",
            "The Jewel of the East Coast and City of Destiny. Unique destination where the verdant Eastern Ghats hills plunge directly into the waters of the Bay of Bengal, featuring pristine coastal drives, submarine museums, and the Borra Caves.",
            "/images/dest_visakhapatnam.jpg",
            17.6868, 83.2185, 2700, "October to March",
            new Attraction[] {
                new Attraction("viz-1", "INS Kursura Submarine Museum", "visakhapatnam", "Museum", "Decommissioned Russian Kalvari-class submarine on the sands of RK Beach, first of its kind in Asia.", 40, 4.8, "02:00 PM - 08:30 PM (Closed Mondays)", "/images/attr_viz-1.jpg"),
                new Attraction("viz-2", "Kailasagiri Hill Park", "visakhapatnam", "Scenic", "Hilltop park 360 feet above sea level with a ropeway cable car, giant Shiva-Parvati statue, and coastal bay vistas.", 20, 4.6, "06:00 AM - 07:30 PM", "/images/attr_viz-2.jpg"),
                new Attraction("viz-3", "Rushikonda Beach & Watersports", "visakhapatnam", "Beach", "Golden sand Blue Flag certified beach popular for windsurfing, sea kayaking, and speedboating.", 0, 4.7, "Open 24 Hours", "/images/attr_viz-3.jpg"),
                new Attraction("viz-4", "Borra Caves & Araku Valley", "visakhapatnam", "Nature", "Million-year-old limestone karst caves with natural stalactites and stalagmites in the Ananthagiri hills.", 80, 4.8, "10:00 AM - 05:00 PM", "/images/attr_viz-4.jpg")
            },
            new Hotel[] {
                new Hotel("h-viz-1", "The Gateway Hotel Beach Road", "visakhapatnam", "Luxury", 9500, 4.7, "Beach Road", new String[]{"Bay View Rooms", "Seafood Dining", "Pool"}),
                new Hotel("h-viz-2", "Novotel Visakhapatnam Varun Beach", "visakhapatnam", "Luxury", 11000, 4.8, "RK Beach Promenade", new String[]{"Infinity Pool over Ocean", "Jogging Track", "Spa"}),
                new Hotel("h-viz-3", "Dolphin Hotel", "visakhapatnam", "Mid-Range", 3800, 4.3, "Daba Gardens", new String[]{"Central Location", "Multi-Cuisine"})
            },
            new Restaurant[] {
                new Restaurant("r-viz-1", "Sea Inn (Raju Gari Dhaba)", "visakhapatnam", "Andhra Seafood", 900, 4.6, "Spicy Andhra Prawn Fry & Crab Masala", "Rushikonda Beach Road"),
                new Restaurant("r-viz-2", "Tycoon Cultural Cuisine", "visakhapatnam", "South Indian & Andhra", 1100, 4.5, "Ulavacharu Biryani & Royyala Vepudu", "VIP Road"),
                new Restaurant("r-viz-3", "Daspalla Executive Court", "visakhapatnam", "Traditional Andhra", 700, 4.6, "Pure Ghee Andhra Bhojanam Thali", "Waltair Uplands")
            },
            new Transport[] {
                new Transport("t-viz-1", "visakhapatnam", "Flight", "Visakhapatnam International Airport (VTZ)", "Direct flights to Singapore, Kuala Lumpur, and all Indian metros", 3800, "NAD Junction, 8km west"),
                new Transport("t-viz-2", "visakhapatnam", "Train", "Visakhapatnam Junction (VSKP)", "East Coast Railway terminal; origin of Vande Bharat to Secunderabad", 850, "Major coastal junction"),
                new Transport("t-viz-3", "visakhapatnam", "Road", "Beach Road Scenic Marine Drive", "Continuous 30km cliffside coastal drive from RK Beach to Bheemili", 250, "Buses, autos, and shared cabs")
            }
        )
    };

    /**
     * Authentic Intercity Travel Connections across India.
     * Incorporates real highway/rail distances (km), travel times (minutes), monetary costs (INR),
     * and daily passenger/seat capacities for the Edmonds-Karp Transport Capacity Network.
     */
    public static final TravelEdge[] TRAVEL_EDGES = new TravelEdge[] {
        // Hyderabad Hub
        new TravelEdge("hyderabad", "bengaluru", 570, 510, 1400, 4200, "Highway/Train"),
        new TravelEdge("bengaluru", "hyderabad", 570, 510, 1400, 4200, "Highway/Train"),
        new TravelEdge("hyderabad", "chennai", 630, 580, 1500, 3500, "Highway/Train"),
        new TravelEdge("chennai", "hyderabad", 630, 580, 1500, 3500, "Highway/Train"),
        new TravelEdge("hyderabad", "goa", 660, 690, 1600, 2400, "Highway/Train"),
        new TravelEdge("goa", "hyderabad", 660, 690, 1600, 2400, "Highway/Train"),
        new TravelEdge("hyderabad", "mumbai", 710, 660, 1700, 4500, "Highway/Train"),
        new TravelEdge("mumbai", "hyderabad", 710, 660, 1700, 4500, "Highway/Train"),
        new TravelEdge("hyderabad", "pune", 560, 520, 1350, 3800, "Highway/Train"),
        new TravelEdge("pune", "hyderabad", 560, 520, 1350, 3800, "Highway/Train"),
        new TravelEdge("hyderabad", "visakhapatnam", 620, 570, 1450, 3200, "Highway/Train"),
        new TravelEdge("visakhapatnam", "hyderabad", 620, 570, 1450, 3200, "Highway/Train"),

        // Delhi Hub
        new TravelEdge("delhi", "jaipur", 280, 210, 750, 6500, "Expressway/Train"),
        new TravelEdge("jaipur", "delhi", 280, 210, 750, 6500, "Expressway/Train"),
        new TravelEdge("delhi", "agra", 210, 120, 600, 8000, "Expressway/Train"),
        new TravelEdge("agra", "delhi", 210, 120, 600, 8000, "Expressway/Train"),
        new TravelEdge("delhi", "rishikesh", 240, 270, 650, 3900, "Highway/Train"),
        new TravelEdge("rishikesh", "delhi", 240, 270, 650, 3900, "Highway/Train"),
        new TravelEdge("delhi", "amritsar", 450, 330, 1100, 5000, "Expressway/Train"),
        new TravelEdge("amritsar", "delhi", 450, 330, 1100, 5000, "Expressway/Train"),
        new TravelEdge("delhi", "varanasi", 820, 480, 1900, 4200, "Vande Bharat/Highway"),
        new TravelEdge("varanasi", "delhi", 820, 480, 1900, 4200, "Vande Bharat/Highway"),
        new TravelEdge("delhi", "manali", 530, 690, 1400, 2800, "Highway/Volvo"),
        new TravelEdge("manali", "delhi", 530, 690, 1400, 2800, "Highway/Volvo"),

        // Bengaluru Hub
        new TravelEdge("bengaluru", "chennai", 350, 270, 950, 7200, "Expressway/Train"),
        new TravelEdge("chennai", "bengaluru", 350, 270, 950, 7200, "Expressway/Train"),
        new TravelEdge("bengaluru", "mysuru", 145, 90, 450, 8500, "10-Lane Expressway"),
        new TravelEdge("mysuru", "bengaluru", 145, 90, 450, 8500, "10-Lane Expressway"),
        new TravelEdge("bengaluru", "kochi", 550, 570, 1350, 3400, "Highway/Train"),
        new TravelEdge("kochi", "bengaluru", 550, 570, 1350, 3400, "Highway/Train"),
        new TravelEdge("bengaluru", "ooty", 270, 330, 850, 2800, "Scenic Highway"),
        new TravelEdge("ooty", "bengaluru", 270, 330, 850, 2800, "Scenic Highway"),
        new TravelEdge("bengaluru", "goa", 560, 600, 1400, 2900, "Highway/Train"),
        new TravelEdge("goa", "bengaluru", 560, 600, 1400, 2900, "Highway/Train"),

        // Mumbai & Pune Western Corridor
        new TravelEdge("mumbai", "pune", 150, 150, 500, 9500, "Mumbai-Pune Expressway"),
        new TravelEdge("pune", "mumbai", 150, 150, 500, 9500, "Mumbai-Pune Expressway"),
        new TravelEdge("mumbai", "goa", 580, 540, 1400, 4800, "Vande Bharat/Konkan Rail"),
        new TravelEdge("goa", "mumbai", 580, 540, 1400, 4800, "Vande Bharat/Konkan Rail"),
        new TravelEdge("pune", "goa", 460, 490, 1250, 3100, "Highway"),
        new TravelEdge("goa", "pune", 460, 490, 1250, 3100, "Highway"),
        new TravelEdge("mumbai", "udaipur", 760, 720, 1750, 2900, "Highway/Train"),
        new TravelEdge("udaipur", "mumbai", 760, 720, 1750, 2900, "Highway/Train"),

        // Rajasthan Heritage Golden Triangle
        new TravelEdge("jaipur", "agra", 240, 220, 600, 5200, "National Highway"),
        new TravelEdge("agra", "jaipur", 240, 220, 600, 5200, "National Highway"),
        new TravelEdge("jaipur", "udaipur", 390, 360, 950, 3800, "Express Highway"),
        new TravelEdge("udaipur", "jaipur", 390, 360, 950, 3800, "Express Highway"),
        new TravelEdge("agra", "varanasi", 610, 450, 1450, 3900, "Agra-Lucknow Expressway"),
        new TravelEdge("varanasi", "agra", 610, 450, 1450, 3900, "Agra-Lucknow Expressway"),

        // Northern Mountain & Sacred Corridors
        new TravelEdge("rishikesh", "amritsar", 420, 480, 1100, 2400, "Highway"),
        new TravelEdge("amritsar", "rishikesh", 420, 480, 1100, 2400, "Highway"),
        new TravelEdge("amritsar", "manali", 410, 520, 1150, 2100, "Mountain Highway"),
        new TravelEdge("manali", "amritsar", 410, 520, 1150, 2100, "Mountain Highway"),
        new TravelEdge("rishikesh", "manali", 490, 630, 1350, 1800, "Scenic Himalayan Route"),
        new TravelEdge("manali", "rishikesh", 490, 630, 1350, 1800, "Scenic Himalayan Route"),

        // Southern Connections
        new TravelEdge("mysuru", "ooty", 125, 180, 400, 2700, "Bandipur Forest Corridor"),
        new TravelEdge("ooty", "mysuru", 125, 180, 400, 2700, "Bandipur Forest Corridor"),
        new TravelEdge("chennai", "kochi", 690, 660, 1600, 3100, "Southern Express"),
        new TravelEdge("kochi", "chennai", 690, 660, 1600, 3100, "Southern Express"),
        new TravelEdge("kochi", "ooty", 280, 360, 900, 2200, "Ghat Highway"),
        new TravelEdge("ooty", "kochi", 280, 360, 900, 2200, "Ghat Highway"),

        // Eastern Corridor
        new TravelEdge("kolkata", "varanasi", 680, 540, 1600, 4200, "Grand Trunk / Rail"),
        new TravelEdge("varanasi", "kolkata", 680, 540, 1600, 4200, "Grand Trunk / Rail"),
        new TravelEdge("kolkata", "darjeeling", 610, 570, 1500, 3600, "Highway/Train"),
        new TravelEdge("darjeeling", "kolkata", 610, 570, 1500, 3600, "Highway/Train"),
        new TravelEdge("kolkata", "visakhapatnam", 880, 780, 2100, 3400, "NH 16 East Coast"),
        new TravelEdge("visakhapatnam", "kolkata", 880, 780, 2100, 3400, "NH 16 East Coast"),
        new TravelEdge("visakhapatnam", "chennai", 800, 720, 1900, 3800, "NH 16 East Coast"),
        new TravelEdge("chennai", "visakhapatnam", 800, 720, 1900, 3800, "NH 16 East Coast")
    };

    /**
     * Finds a Destination by its unique ID.
     */
    public static Destination getDestinationById(String id) {
        if (id == null) return null;
        for (Destination d : DESTINATIONS) {
            if (d.getId().equalsIgnoreCase(id)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Finds the index of a destination in DESTINATIONS array.
     */
    public static int getDestinationIndex(String id) {
        if (id == null) return -1;
        for (int i = 0; i < DESTINATIONS.length; i++) {
            if (DESTINATIONS[i].getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }
}
