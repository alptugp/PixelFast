module KMeans where

type Point = (Int, Int)

type Centroid = Point

type ClusterIndex = Int

type Assignment = (Point, ClusterIndex)

type Clustering = ([Assignment], [Centroid])

type Colouring = ([(Point, Colour)], [Centroid])

data Colour = Black | Red | Green | Blue | Mauve
  deriving (Show, Enum, Eq)

lookUpAll :: Eq a => a -> [(b, a)] -> [b]
lookUpAll k kvps 
  = [v | (v, k') <- kvps, k == k']

distance :: Point -> Point -> Int
distance (x, y) (x', y')
  = abs (x - x') + abs (y - y')

sumPoints :: Point -> Point -> Point
sumPoints (x, y) (x', y')
  = (x + x', y + y')

centroid :: [Point] -> Centroid
centroid pList
  = (div (foldl (\sx (x, _) -> sx + x) 0 pList) n, div (foldl (\sy (_, y) -> sy + y) 0 pList) n)
  where 
    n = length pList

assign :: Point -> [Centroid] -> Assignment
assign p cList
  = (p, snd (minimum (zipWith (\i c -> (distance p c, i)) [1..] cList)))
  
assignAll :: [Point] -> [Centroid] -> [Assignment]
assignAll ps cList
  = map (flip (assign) cList) ps

adjustCentroids :: [Assignment] -> Int -> [Centroid]
adjustCentroids as n  
  = map (\i -> centroid (lookUpAll i as)) [1..n]

cluster :: [Assignment] -> [Centroid] -> Int -> Clustering
--Pre: ps contains at least k items
cluster as cs n
  | ncs == cs && as == nas = (nas, ncs)
  | otherwise = cluster nas ncs n
  where 
    ncs = adjustCentroids as n
    ps = [p | (p, cl) <- as]
    nas = assignAll ps ncs

kmeans :: [Point] -> Int -> Clustering
-- Pre: ps has at least k elements
kmeans ps k 
  = cluster assgnedPs initialCs k
  where 
    initialCs = take k ps
    assgnedPs = assignAll ps initialCs
    
-- Bonus question
--
applyColours :: Clustering -> Colouring
-- Pre: There are at least as many colours as there are
-- clusters
applyColours (assgnmnts, cs)
  = ([(p, toEnum (i - 1)::Colour) | (p, i) <- assgnmnts], cs)
  
  -- = (map (\(p, i) -> (p, toEnum (i - 1)::Colour)) assgnmnts, cs)
---------------------------------------------------------------
-- Test data corresponding to Figure 1

points :: [Point]
points = [(3,1),(2,9),(5,6),(7,8),(2,3),(6,7),(3,5),(4,9),(9,3),
          (1,1), (2,1),(9,1),(7,2),(3,7)]

fig1aAssignment :: [Assignment]
fig1aAssignment
  = [((3,1),1),((2,9),2),((5,6),3),((7,8),3),((2,3),1),((6,7),3),
     ((3,5),3),((4,9),2),((9,3),3),((1,1),1),((2,1),1),((9,1),1),
     ((7,2),1),((3,7),2)]

fig1bAdjustedCentroids :: [Centroid]
fig1bAdjustedCentroids
  = [(4,1),(3,8),(6,5)]

fig1bAssignment :: [Assignment]
fig1bAssignment
  = [((3,1),1),((2,9),2),((5,6),3),((7,8),2),((2,3),1),((6,7),3),
     ((3,5),2),((4,9),2),((9,3),3),((1,1),1),((2,1),1),((9,1),1),
     ((7,2),1),((3,7),2)]

-- Result of kmeans points 3...
result :: Clustering
result
  = ([((3,1),1),((2,9),2),((5,6),3),((7,8),3),((2,3),1),((6,7),3),
      ((3,5),2),((4,9),2),((9,3),3),((1,1),1),((2,1),1),((9,1),1),
      ((7,2),1),((3,7),2)],
     [(4,1),(3,7),(6,6)])

---------------------------------------------------------------
-- A couple of other test sets

points2 :: [Point]
points2
  = [(10,10),(50,70),(15,20),(30,40),(35,50),(45,50),(35,45)]

-- Result of kmeans points2 2
result2 :: Clustering
result2
 = ([((10,10),1),((50,70),2),((15,20),1),((30,40),2),((35,50),2),
     ((45,50),2),((35,45),2)],
    [(12,15),(39,51)])

points3 :: [Point]
points3
 = [(259,307), (198,308), (354,156), (388,166), (268,324), (296,332),
    (308,178), (286,195), (368,231), (276,284), (292,334), (141,152),
    (337,333), (316,106), (365,112), (345,186), (286,317), (280,132),
    (353,184), (378,224), (359,234), (155,234), (208,232), (390,182),
    (274,301), (303,306), (316,246), (365,234), (371,129), (43,289)]

-- Result of kmeans points3 4
result3 :: Clustering
result3
 = ([((259,307),1),((198,308),1),((354,156),3),((388,166),4),((268,324),1),
     ((296,332),1),((308,178),3),((286,195),3),((368,231),4),((276,284),1),
     ((292,334),1),((141,152),2),((337,333),1),((316,106),3),((365,112),3),
     ((345,186),4),((286,317),1),((280,132),3),((353,184),4),((378,224),4),
     ((359,234),4),((155,234),2),((208,232),2),((390,182),4),((274,301),1),
     ((303,306),1),((316,246),4),((365,234),4),((371,129),3),((43,289),2)],
    [(278,314),(136,226),(325,144),(362,209)])
