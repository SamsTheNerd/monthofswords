package com.samsthenerd.monthofswords.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public class BFSHelper {
    public static Map<BlockPos, Integer> runBFS(World world, BlockPos startingPos,
                                       BFSPredicate predicate, int depth, boolean checkDiagonals){
        BFSNeighbors<BlockPos> straightNeighbors = (fromPos, dist) -> {
            List<BlockPos> neighbors = new ArrayList<>(6);
            for(Direction dir : Direction.values()){
                BlockPos newPos = fromPos.offset(dir);
                if(predicate.test(world, fromPos, dist+1)){
                    neighbors.add(newPos);
                }
            }
            return neighbors;
        };

        BFSNeighbors<BlockPos> gayNeighbors = (fromPos, dist) -> {
            List<BlockPos> neighbors = new ArrayList<>();
            for(int x = -1; x <= 1; x++){
                for(int y = -1; y <= 1; y++){
                    for(int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;
                        BlockPos newPos = fromPos.add(x, y, z);
                        if (predicate.test(world, fromPos, dist + 1)) {
                            neighbors.add(newPos);
                        }
                    }
                }
            }
            return neighbors;
        };

        return runBFS(startingPos, checkDiagonals ? gayNeighbors : straightNeighbors, depth);
    }

    public static <T> Map<T, Integer> runBFS(T initial,
                                       BFSNeighbors<T> cityPlanner, int depth){
        Map<T, Integer> visited = new HashMap<>();
        Queue<T> queue = new LinkedList<>();
        visited.put(initial, 0);
        queue.add(initial);
        while(!queue.isEmpty()){
            T procPoint = queue.poll();
            int dist = visited.get(procPoint);
            if(dist+1 > depth) continue;
            for(T nbr : cityPlanner.getNeighbors(procPoint, dist)){
                if(visited.containsKey(nbr)) continue;
                visited.put(nbr, dist + 1);
                queue.add(nbr);
            }
        }
        return visited;
    }

    public interface BFSNeighbors<T>{
        Collection<T> getNeighbors(T center, int dist);
    }

    @FunctionalInterface
    public interface BFSPredicate {
        boolean test(World world, BlockPos pos, int dist);
    }
}
